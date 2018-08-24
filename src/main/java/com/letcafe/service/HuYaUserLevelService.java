package com.letcafe.service;

import com.letcafe.bean.HuYaUserLevel;

public interface HuYaUserLevelService {
    void save(HuYaUserLevel huYaUserLevel);

    void saveLoginCookie(String cookieKey, String cookieValue);

    String getLoginCookie(String cookieRedisKey);
}
