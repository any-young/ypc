package com.zk.test;

import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.Watcher.Event.EventType.*;
import static org.apache.zookeeper.Watcher.Event.KeeperState.*;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/11/27
 */
@Data
public class ZkTest {

    private static final String ip = "192.168.30.0";
    private static final String port = "2181";
    private static final String timeout = "";
    private static CountDownLatch count = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) {
        try {

            zooKeeper = new ZooKeeper(ip + ":" + port, 5000, event -> {
                if (SyncConnected.equals(event.getState())) {
                    System.out.println("连接成功，连接状态为：" + event.getState());
                    count.countDown();
                    switch (event.getType()) {
                        case NodeChildrenChanged:
                            System.out.println("子节点变化" + event.getPath());
                            break;
                        case NodeCreated:
                            System.out.println("创建新的节点" + event.getPath());
                            break;
                        case NodeDeleted:
                            System.out.println("节点删除：" + event.getPath());
                            break;
                        case NodeDataChanged:
                            System.out.println("节点数据变化：" + event.getPath());
                            try {
                                System.out.println("数据为：" + new String(zooKeeper.getData(event.getPath(), true, stat)));
                            } catch (KeeperException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case None:
                            System.out.println("连接中...");
                            break;
                    }
                }


            });
            count.await();
            if (zooKeeper.exists("/ypc", true) == null) {
                zooKeeper.create("/ypc", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zooKeeper.getData("/ypc", true, new Stat());
            zooKeeper.setData("/ypc", "456".getBytes(), -1);

            for (int i = 0; i < 10; i++) {
                zooKeeper.create("/ypc/service1", ("url"+i).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            }
            List<String> childrens = zooKeeper.getChildren("/ypc", true);
            for (String path : childrens) {
                if (!new String(zooKeeper.getData("/ypc/"+path,true,stat)).equals("url9")){
                    zooKeeper.delete("/ypc/" + path, -1);
                }else {
                    zooKeeper.setData("/ypc/"+path,"url10".getBytes(),-1);
                }
                Thread.sleep(1000l);
            }
            zooKeeper.delete("/ypc", -1);
            Thread.sleep(1000l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
