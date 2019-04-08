package com.lcydream.open.zkpubsub;

import com.lcydream.open.zkpubsub.basics.ZookeeperClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.UUID;


public class ZkPublishEvent {

    //根节点
    private static final String ROOT_PUB="/publish";

    /**
     *  发布方法
     * @param data 发布的数据
     * @param name 使用那个ZookeeperClient客户端
     */
    public static void publish(String data,String name){
        try {
            ZooKeeper zooKeeper = ZookeeperClient.getInstance(name).getZk();
            String PATH = ROOT_PUB + ZookeeperClient.getInstance(name).getPath();
            Stat exists = zooKeeper.exists(PATH, false);
            if(exists == null) {
                ZookeeperClient.createPath(name,PATH);
            }
            if(data == null) {
                zooKeeper.setData(PATH, UUID.randomUUID().toString().getBytes(), -1);
            }else {
                zooKeeper.setData(PATH, data.getBytes(), -1);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
