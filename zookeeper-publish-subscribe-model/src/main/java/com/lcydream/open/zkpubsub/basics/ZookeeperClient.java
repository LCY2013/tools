package com.lcydream.open.zkpubsub.basics;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {

    private static String defaultname = "defaultname";

    private String name;

    private  String CONNECTSTRING="127.0.0.1:2181";

    private  int sessionTimeout = 30000;

    private  String Path = "/zookeeper/watch";

    private ZooKeeper zooKeeper;

    private static Map<String, ZookeeperClient> zookeeperClientMap = new ConcurrentHashMap<>();

    public ZookeeperClient(String name, String CONNECTSTRING, int sessionTimeout, String path) {
        if(name != null && !"".equals(name.trim())) {
            this.name = name;
        }
        if(CONNECTSTRING != null && !"".equals(CONNECTSTRING.trim())) {
            this.CONNECTSTRING = CONNECTSTRING;
        }
        if(sessionTimeout > 0) {
            this.sessionTimeout = sessionTimeout;
        }
        if(path != null && !"".equals(path.trim())) {
            Path = path;
        }
        zookeeperClientMap.put(this.name,this);
    }

    public static ZookeeperClient getInstance(String name){
        if(name!=null && !"".equals(name.trim())) {
            return zookeeperClientMap.get(name);
        }
        return zookeeperClientMap.get(defaultname);
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout){
        if(sessionTimeout != null && sessionTimeout > 0) {
            this.sessionTimeout = sessionTimeout;
        }
    }

    public void setConnectstring(String connect){
        this.CONNECTSTRING = connect;
    }

    public void setPath(String path){
        if(path != null) {
            this.Path = path;
        }
    }

    public String getPath(){
        return this.Path;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized ZooKeeper getZk() {
        if(zooKeeper == null){
            synchronized(ZookeeperClient.class) {
                final CountDownLatch countDownLatch =
                        new CountDownLatch(1);
                try {
                    zooKeeper = new ZooKeeper(this.CONNECTSTRING, this.sessionTimeout, (WatchedEvent event) -> {
                        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                            countDownLatch.countDown();
                        }
                    });
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return zooKeeper;
    }

    public static void createPath(String name,String pathVal){
        String[] paths = pathVal.split("/");
        StringBuffer createPath = new StringBuffer();
        ZooKeeper zk = ZookeeperClient.getInstance(name).getZk();
        for(String path : paths){
            if(path!=null && !"".equals(path)){
                try {
                    Stat exists = zk.exists(createPath.append("/").append(path).toString(), false);
                    if(exists == null){
                        zk.create(createPath.toString(), null,
                                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                                CreateMode.PERSISTENT);
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
