package com.lcydream.open.zkpubsub;

import com.lcydream.open.zkpubsub.basics.ZookeeperClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZkSubscriptEvent {

    //根节点
    private static final String ROOT_PUB="/publish";

    /**
     *
     * @param name com.lcydream.open.zkpubsub.basics.ZookeeperClient 的名称
     * @param watcher 自己实现的监听器
     */
    public static void subScript(String name, Watcher watcher){
        ZooKeeper zooKeeper = ZookeeperClient.getInstance(name).getZk();
        String PATH = ROOT_PUB + ZookeeperClient.getInstance(name).getPath();
        try {
            Stat exists = zooKeeper.exists(PATH, false);
            if(exists == null) {
                ZookeeperClient.createPath(name,PATH);
            }
            zooKeeper.exists(PATH, watcher);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
