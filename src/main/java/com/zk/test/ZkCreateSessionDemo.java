package com.zk.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/11/28
 */
public class ZkCreateSessionDemo {
    private static Logger logger = LoggerFactory.getLogger(ZkCreateSessionDemo.class);
    private static final String CONNECT_STRING = "192.168.30.0:2181";
    private static CuratorFramework curatorFramework = null;

    private static class ZkCreator{
        private static  CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString(CONNECT_STRING)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("ypc")
                .build();
    }

    public static CuratorFramework getInstance() {
        ZkCreator.curatorFramework.start();
        return ZkCreator.curatorFramework;
    }

  /*  public static void main(String[] args) {
        try {
            PropertyConfigurator.configure("D:/log4j.properties");
            //getInstance().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/ay", "123".getBytes());
           Stat stat = new Stat();
            byte[] bytes = getInstance().getData().storingStatIn(stat).forPath("/ay");
            System.out.println(new String(bytes)+"stat: "+stat);
            Thread.sleep(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public static void main(String[] args) {
        CuratorFramework curatorFramework = getInstance();
        System.out.println("连接成功！");

        try {
            String result  = curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath("/curator/curator1");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            curatorFramework.delete().deletingChildrenIfNeeded()
                    .forPath("/curator/curator1");
            System.out.println("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

        }catch (Exception e){

        }

        /**
         * 异步操作
         */
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            CountDownLatch count = new CountDownLatch(1);
            curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .inBackground((client, event) -> {
                        System.out.println(Thread.currentThread().getName()+" resultCode + :"+event.getResultCode());
                        count.countDown();
                    },executorService)
                    .forPath("/curator/ay");
            count.await();
            System.out.println("程序结束！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 事务操作是curator独有的
         * 可以通过api能够操作事务
         */

        try {
            Collection<CuratorTransactionResult> transactionResults = curatorFramework.inTransaction()
                    .create().forPath("/abc")
                    .and().setData().forPath("/curator","123".getBytes()).and().commit();
            transactionResults.stream().forEach( rs -> System.out.println(rs.getResultStat()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * client的事件监视
         *
         * pathCache
         * NodeCache
         * TreeCache
         */

        //先看nodeCache，第三个参数是缓存数据是不是压缩
        NodeCache nodeCache = new NodeCache(curatorFramework, "/curator", false);
        try {
            nodeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nodeCache.getListenable().addListener(()->{
            System.out.println("节点数据发生变化："+nodeCache.getCurrentData().getData());
        });

        try {
            curatorFramework.setData().forPath("/curator","lyt".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //PathChildren,持续监听子节点，子节点的增删改查的监听
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,"/curator",false);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}