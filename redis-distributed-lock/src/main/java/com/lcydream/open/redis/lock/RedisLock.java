package com.lcydream.open.redis.lock;

import com.lcydream.open.redis.common.RedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

public class RedisLock {

    public static Jedis getJedis(){
        return RedisPool.getJedis();
    }

    /**
     *  获取锁资源
     * @param key 锁对象
     * @param timeout 等待的超时时间，单位ms,默认3000ms
     * @return 获取是否成功，成功返回{@code String}UUID,失败返回{@code null}
     */
    public String getLock(String key,Long timeout,boolean isReturn){
        //jedis客户端
        Jedis jedisClient = null;
        try {
            jedisClient = getJedis();
            String value = UUID.randomUUID().toString();
            //默认值给一个3毫秒
            if(timeout == null || timeout < 0){
                timeout = 3000L;
            }
            //计算出一个结束时间
            long endTime = System.currentTimeMillis()+timeout;
            while (endTime > System.currentTimeMillis()) {
                //设置key成功
                if (1L == jedisClient.setnx(key, value)) {
                    if(!isReturn) {
                        //设置key的过期时间
                        jedisClient.expire(key, (int) (timeout / 1000 == 0 ? 1 : timeout / 1000));
                    }else {
                        jedisClient.expire(key, 1000);
                    }
                    return value;
                }
                if(isReturn){
                    return null;
                }
                //如果在设置key成功后redis连接失败，我们就需要在这里判断是否设置了过期时间
                if(-1 == jedisClient.ttl(key)){
                    if(!isReturn) {
                        //设置key的过期时间
                        jedisClient.expire(key, (int) (timeout / 1000 == 0 ? 1 : timeout / 1000));
                    }else {
                        jedisClient.expire(key, 1000);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedisClient != null){
                jedisClient.close();
            }
        }
        return null;
    }

    /**
     * 释放锁
     * @param key 锁字段
     * @return 是否释放成功
     */
    public boolean releaseLock(String key,String value){
        //jedis客户端
        Jedis jedisClient = null;
        //判断参数是否合法
        if(key == null || value == null){
            return true;
        }
        try {
            jedisClient = getJedis();
            while (true) {
                //观察这个key的动态
                jedisClient.watch(key);
                if (value.equals(jedisClient.get(key))) {
                    //开启redis事物
                    Transaction multi = jedisClient.multi();
                    //删除锁对象
                    multi.del(key);
                    //提交事物
                    List<Object> exec = multi.exec();
                    if(null == exec){
                        continue;
                    }
                    return true;
                }
                //取消观察这个key
                jedisClient.unwatch();
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != jedisClient){
                jedisClient.close();
            }
        }
        return false;
    }

}