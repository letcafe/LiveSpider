package com.letcafe.service.impl;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.dao.HuYaUserLevelDao;
import com.letcafe.dao.RedisDao;
import com.letcafe.service.HuYaUserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HuYaUserLevelServiceImpl implements HuYaUserLevelService {

    private HuYaUserLevelDao userLevelDao;
    private RedisDao redisDao;

    @Autowired
    public HuYaUserLevelServiceImpl(HuYaUserLevelDao userLevelDao, RedisDao redisDao) {
        this.userLevelDao = userLevelDao;
        this.redisDao = redisDao;
    }

    @Override
    public void save(HuYaUserLevel huYaUserLevel) {
        userLevelDao.save(huYaUserLevel);
    }

    @Override
    public void saveLoginCookie(String cookieKey, String cookieValue) {
        redisDao.setKeyValueWithExpireTime(cookieKey, cookieValue, 6 * 24 * 3600 * 1000);
    }

    @Override
    public String getLoginCookie(String cookieRedisKey) {
        return redisDao.getStringValue(cookieRedisKey);
    }
}
