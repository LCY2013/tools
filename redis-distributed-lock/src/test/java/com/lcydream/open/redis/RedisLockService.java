package com.lcydream.open.redis;

import com.lcydream.open.redis.lock.RedisLock;

public class RedisLockService {

    private static final RedisLock redisLock = new RedisLock();

    public static void main(String[] args) {
        String lockKey = "lockKey";
        String lockValue = "";
        try {
            if (null != (lockValue = redisLock.getLock(lockKey, 10000L, false))) {
                //做业务处理
                //doService()
            }
        }finally {
            redisLock.releaseLock(lockKey,lockValue);
        }
    }

}
