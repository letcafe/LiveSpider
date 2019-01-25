package io.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;


public class RedisStudy {
    static int[] precision = new int[]{1, 5, 60, 300, 3600, 1800, 86400};
    private Jedis jedis;

    @Before
    public void connectToRedis() {
//        jedis = new Jedis("172.18.18.10", 6482);
        jedis = new Jedis("localhost", 6379);
        jedis.auth("redisServer");
        jedis.flushDB();
    }

    @Test
    public void testRedisString() throws IOException, InterruptedException {
        String testKey = "helloWorlds";
        Random random = new Random(47);
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(100);
            jedis.get(testKey + 1);
            System.out.println("redis size = " + jedis.dbSize());
        }
    }

    public String aquireLock(String lockName, long timeout) throws InterruptedException {
        String identifier = UUID.randomUUID().toString();
        long endTime = System.currentTimeMillis() + timeout;
        if (System.currentTimeMillis() < endTime) {
            Long getLockResult = jedis.setnx("lock:" + lockName, identifier);
            if (getLockResult != 0) {
                return identifier;
            }
            Thread.sleep(1000);
        }
        return "false";
    }


    public boolean releaseLock(String lockName, String identifier) {
        Pipeline pipeline = jedis.pipelined();

        while (true) {
            pipeline.watch(lockName);
            if (pipeline.get(lockName).toString().equals(identifier)) {
                pipeline.multi();
                pipeline.del(lockName);
                pipeline.exec();
                return true;
            }
            pipeline.exec();
            break;
        }
        return false;
    }
}