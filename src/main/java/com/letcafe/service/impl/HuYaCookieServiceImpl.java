package com.letcafe.service.impl;

import com.letcafe.bean.HuYaProperties;
import com.letcafe.service.CookieService;
import com.letcafe.dao.RedisDao;
import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class HuYaCookieServiceImpl implements CookieService {

    private static final Logger logger = LoggerFactory.getLogger(HuYaCookieServiceImpl.class);

    private RedisDao redisDao;
    private HuYaProperties huYaProperties;
    private WebDriver webDriver;

    @Autowired
    public HuYaCookieServiceImpl(RedisDao redisDao, HuYaProperties huYaProperties) {
        this.redisDao = redisDao;
        this.huYaProperties = huYaProperties;
        this.webDriver = webDriver;
    }

    @Override
    public void setUserCookieInRedis(String username, String password) {
        for (int i = 1; i <= huYaProperties.getAttemptTimes(); i++) {
            String cookieIntoRedis = HuYaUtils.login(webDriver, username, password);
            // 如果找到了正确的Cookie
            if (cookieIntoRedis != null && cookieIntoRedis.contains(huYaProperties.getCheckLoginStr())) {
                logger.warn("[Cookie Setter] login success, loginCookie:" + username + "  => " + cookieIntoRedis);
                // Redis中的值将保留六天，配合周三周日刷新，不会失效(后面的计算6天的值，会由编译器进行优化编译)
                redisDao.setKeyValueWithExpireTime("loginCookie:" + username, cookieIntoRedis, 6 * 24 * 60 * 60 * 1000);
                return;
            }
            // 如果没找到，开始重试
            logger.warn("[Cookie Setter] login failed, try " + i + " times");
        }
        // 重试超过失败次数，打印错误日志
        logger.error("[Cookie Setter] attempt " + huYaProperties.getAttemptTimes() + " times failed");
    }

    @Override
    public String getUserCookieInRedis(String username) {
        return redisDao.getStringValue(huYaProperties.getCookieInRedis());
    }
}
