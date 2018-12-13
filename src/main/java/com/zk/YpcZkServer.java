package com.zk;

import com.balance.net.RemoteNode;
import com.balance.net.YpcURI;
import com.collection.Cache;
import com.collection.ConcurrentCache;
import com.protocol.ProtocolSelector;
import com.protocol.Serializer;
import com.zk.event.YpcChildEventHandler;
import com.zk.event.YpcEventHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/12/4
 */
public class YpcZkServer implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(YpcZkServer.class);
    private static final String SLASH = "/";
    private static final String NAME_SPACE = "ypc";
    private static final String NODE_NAME = "node_";
    public static Cache<String, List<RemoteNode>> REMOTE_NODES = new ConcurrentCache<>();
    private YpcEventHandler ypcEventHandler;
    private Integer protocol = 1;
    private static Serializer serializer;
    private CuratorFramework curatorFramework;
    private String zkAddress;

    /**
     * 获取数据失败返回null
     *
     * @param path
     * @return
     */
    private byte[] getData(String path) {
        byte[] bytes = null;
        try {
            bytes = curatorFramework.getData().forPath(path);
        } catch (Exception e) {
            log.error("get data failed...", e);
        }
        return bytes;
    }

    /**
     * 创建节点失败只做日志，不把异常抛出去
     *
     * @param uri
     * @throws Exception
     */
    private void createNode(YpcURI uri, String nodePath) {
        try {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(nodePath, serializeToBytes(uri));
        } catch (Exception e) {
            log.error("create node failed...", e);
        }
    }

    private List<String> getChildren(String node) {
        List<String> children = null;
        try {
            children = curatorFramework.getChildren().forPath(supplyPath(node));
        } catch (Exception e) {
            log.error("get children failed...", e);
        }
        return children;
    }

    /**
     * 初始化providers
     *
     * @param providers
     * @param port
     */
    public void initProviders(HashSet<String> providers, String port) {
        try {
            InetAddress localHost = Inet4Address.getLocalHost();
            YpcURI uri = new YpcURI(localHost.getHostAddress(), port);
            for (String provider : providers) {
                String providerPath = supplyPath(provider, NODE_NAME);
                createNode(uri, providerPath);
            }
        } catch (Exception e) {
            log.error("init service failed...", e);
        }
    }

    /**
     * 初始化消费者
     *
     * @param nodes
     * @throws IOException
     */
    public void initConsumers(List<String> nodes) throws IOException {
        for (String node : nodes) {
            List<RemoteNode> remoteNodes = getChildrenData(node);
            if (Objects.nonNull(remoteNodes)) {
                REMOTE_NODES.setCache(node, remoteNodes);
                //对于这个节点注册子节点变化的监听器
                registerChildNodeListener(node);
            } else {
                log.info("no register node for class: " + node);
            }
        }
        log.info(REMOTE_NODES.toString());
    }

    /**
     * 注册服务子节点的变化情况，对于每个class来说，本身为父节点
     * 每个注册到父节点下面的子节点会随着服务的注册和停用增加和删除
     */
    private void registerChildNodeListener(String className) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(this.curatorFramework, supplyPath(className), true);
        PathChildrenCacheListener listener = ((client, event) -> {
            ChildData childData = event.getData();
            if (Objects.nonNull(childData)) {
                YpcURI ypcURI;
                if (notEmptyByteArray(childData.getData())) {
                    ypcURI = serializeToUri(childData.getData());
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            ypcEventHandler.nodeAdd(childData.getPath(), ypcURI);
                            log.info("child node add... node: " + childData.getPath() + " data is : " + ypcURI);
                            break;
                        case CHILD_REMOVED:
                            ypcEventHandler.nodeDelete(childData.getPath());
                            log.info("child node delete... node: " + childData.getPath() + " data is : " + ypcURI);
                            break;
                        case CHILD_UPDATED:
                            ypcEventHandler.nodeUpdate(childData.getPath(), ypcURI);
                            log.info("child node change... node: " + childData.getPath() + " data is : " + ypcURI);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        pathChildrenCache.getListenable().addListener(listener);
        System.out.println("Register zk watcher successfully! for: " + className);
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            log.error("registerChildNodeListener failed... ", e);
        }
    }

    public static boolean notEmptyByteArray(byte[] objects) {
        return Objects.nonNull(objects) && objects.length > 0;
    }

    private List<RemoteNode> getChildrenData(String node) throws IOException {
        List<RemoteNode> remoteNodes = null;
        List<String> children = getChildren(node);
        if (!CollectionUtils.isEmpty(children)) {
            remoteNodes = new LinkedList<>();
            for (int i = 0; i < children.size(); i++) {
                byte[] data = getData(supplyPath(node, children.get(i)));
                remoteNodes.add(new RemoteNode(children.get(i), serializeToUri(data)));
            }
        }
        return remoteNodes;
    }

    public YpcURI serializeToUri(byte[] bytes) throws IOException {
        if (Objects.isNull(serializer)) return null;
        return serializer.transToObject(YpcURI.class, bytes);
    }

    public byte[] serializeToBytes(Object object) throws IOException {
        if (Objects.isNull(serializer)) return null;
        return serializer.transToByte(object);
    }

    /**
     * 补充节点路径
     * 如果有父节点，则在节点前面加上父节点
     *
     * @param farther
     * @param node
     * @return
     */
    private static String supplyPath(String farther, String node) {
        StringBuilder root = new StringBuilder(SLASH);
        if (!StringUtils.isEmpty(farther)) {//如果有父节点，则在前面加上父节点的路径
            root.append(farther).append(SLASH);
        }
        root.append(node);
        return root.toString();
    }

    /**
     * 补充路径
     *
     * @param node
     * @return
     */
    private static String supplyPath(String node) {
        return supplyPath(null, node);
    }


    @Override
    public void afterPropertiesSet() {
        //初始化序列化协议
        serializer = ProtocolSelector.getProtocol(protocol);
        ypcEventHandler = new YpcChildEventHandler();
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .connectionTimeoutMs(1000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 1))
                .namespace(NAME_SPACE)
                .build();
        curatorFramework.start();
    }


    public static Cache<String, List<RemoteNode>> getRemoteNodes() {
        return REMOTE_NODES;
    }

    public static void setRemoteNodes(Cache<String, List<RemoteNode>> remoteNodes) {
        REMOTE_NODES = remoteNodes;
    }

    public YpcEventHandler getYpcEventHandler() {
        return ypcEventHandler;
    }

    public void setYpcEventHandler(YpcEventHandler ypcEventHandler) {
        this.ypcEventHandler = ypcEventHandler;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public static Serializer getSerializer() {
        return serializer;
    }

    public static void setSerializer(Serializer serializer) {
        YpcZkServer.serializer = serializer;
    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    public void setCuratorFramework(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }
}
