package com.lcydream.open.zkpubsub.basics;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

public abstract class AbstractEventWatcher implements Watcher {

    public String name;

    public AbstractEventWatcher(String name) {
        this.name = name;
    }

    public String getData(String path){
        try {
            byte[] data = ZookeeperClient.getInstance(name).getZk().getData(path, true, new Stat());
            return new String(data);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
