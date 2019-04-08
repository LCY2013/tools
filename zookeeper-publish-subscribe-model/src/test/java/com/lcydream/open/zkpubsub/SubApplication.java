package com.lcydream.open.zkpubsub;

import com.lcydream.open.zkpubsub.basics.ZookeeperClient;
import com.lcydream.open.zkpubsub.wacher.ConstomWacher;

public class SubApplication {

    public static void main(String[] args) {
        //注入一个zookeeperClient(如果是spring ioc容器可以利用xml去配置一个
        // com.lcydream.open.zkpubsub.basics.ZookeeperClient,如果是spring boot可以利用AutoConfig去实现配置
        // com.lcydream.open.zkpubsub.basics.ZookeeperClient，如果存在多个发布者和订阅者需要定义不同名称的
        // com.lcydream.open.zkpubsub.basics.ZookeeperClient)
        //这里手动的创建一个com.lcydream.open.zkpubsub.basics.ZookeeperClient
        ZookeeperClient zookeeperClient = new ZookeeperClient(
                "magic luo",
                "192.168.21.161:2181",
                3000,
                "/zookeeper/publish/pub001"
        );

        ZkSubscriptEvent.subScript("magic luo",
                new ConstomWacher(ZookeeperClient.getInstance("magic luo").getName()));
    }
}
