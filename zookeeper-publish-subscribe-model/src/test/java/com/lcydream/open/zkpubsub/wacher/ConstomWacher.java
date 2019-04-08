package com.lcydream.open.zkpubsub.wacher;

import com.lcydream.open.zkpubsub.basics.AbstractEventWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 *  @ClassName: ConstomWacher
 *  @author: LuoChunYun
 *  @Date: 2019/4/8 10:43
 *  @Description: 自定义接收器
 */ 
public class ConstomWacher extends AbstractEventWatcher {

    public ConstomWacher(String name) {
        super(name);
    }

    @Override
    public void process(WatchedEvent event) {
        //处理逻辑
        System.out.println("接受到事件");
    }
}
