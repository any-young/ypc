package com;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException, KeeperException {
        final CountDownLatch count = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.0.3:2181", 4000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (Event.KeeperState.SyncConnected == event.getState()){
                    count.countDown();
                }
            }
        });
        count.await();
        System.out.print(zooKeeper.getState());
        zooKeeper.create("/zk-test","0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        Thread.sleep(1000);
        char[] ch = "abc".toCharArray();
        Stat stat = new Stat();
        byte[] bytes = zooKeeper.getData("/zk-test",null, stat);
        System.out.println("当前节点的值"+new String(bytes));

        //修改节点值
        zooKeeper.setData("/zk-test","1".getBytes(), stat.getVersion());

        bytes = zooKeeper.getData("/zk-test",null, stat);
        System.out.println("当前值："+new String(bytes));

        zooKeeper.close();


    }
}
