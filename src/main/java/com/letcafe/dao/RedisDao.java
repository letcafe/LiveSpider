package com.letcafe.dao;

public interface RedisDao {

    void setKeyValue(String key, String value);

    void setKeyValueWithExpireTime(String key, String value, long expireTime);

    String getStringValue(String key);

    void deleteKey(String key);
}
