package com.letcafe.dao.impl;

import com.letcafe.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisDaoImpl implements RedisDao{

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public void setKeyValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setKeyValueWithExpireTime(String key, String value, long expireTime) {
        stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getStringValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }


}
