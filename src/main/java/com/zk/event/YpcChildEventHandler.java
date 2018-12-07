package com.zk.event;

import com.balance.net.RemoteNode;
import com.balance.net.YpcURI;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zk.YpcZkServer.REMOTE_NODES;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/7
 */
public class YpcChildEventHandler implements YpcEventHandler<YpcURI>{
    @Override
    public void nodeAdd(String path, YpcURI ypcUri) {
        String className = getClassName(path);
        if (!StringUtils.isEmpty(className)){
            List<RemoteNode> remoteNodes =  REMOTE_NODES.getCache(className);
            if (!CollectionUtils.isEmpty(remoteNodes) && !isExist(remoteNodes, path))
            remoteNodes.add(new RemoteNode(path,ypcUri));
            REMOTE_NODES.setCache(className, remoteNodes);
        }
    }

    @Override
    public void nodeUpdate(String path, YpcURI ypcUri) {
        String className = getClassName(path);
        if (!StringUtils.isEmpty(className)){
            List<RemoteNode> remoteNodes =  REMOTE_NODES.getCache(className);
            if (!CollectionUtils.isEmpty(remoteNodes)){
                for (RemoteNode remoteNode : remoteNodes){
                    if (remoteNode.getNodeName().contains(path))
                        remoteNode.setUri(ypcUri);
                }
            }
            REMOTE_NODES.setCache(className, remoteNodes);
        }
    }

    @Override
    public void nodeDelete(String path) {
        String className = getClassName(path);
        if (!StringUtils.isEmpty(className)){
            List<RemoteNode> remoteNodes =  REMOTE_NODES.getCache(className);
            Iterator<RemoteNode> it = remoteNodes.iterator();
            if (it.hasNext()){
                RemoteNode remoteNode = it.next();
                if (remoteNode.getNodeName().contains(path))
                    it.remove();
            }
            REMOTE_NODES.setCache(className, remoteNodes);
        }
    }

    private String getClassName(String path){
        String regex = "/(.*?)/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    private boolean isExist(List<RemoteNode> remoteNodes, String path){
        for (RemoteNode remoteNode : remoteNodes){
            if (remoteNode.getNodeName().contains(path)){
                return true;
            }
        }
        return false;
    }
}
