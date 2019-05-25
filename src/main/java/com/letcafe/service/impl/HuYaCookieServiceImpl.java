package com.letcafe.service.impl;

import com.letcafe.bean.WebDriverFactory;
import com.letcafe.service.CookieService;
import com.letcafe.dao.RedisDao;
import com.letcafe.util.CookieUtils;
import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class HuYaCookieServiceImpl implements CookieService {

    private static final Logger logger = LoggerFactory.getLogger(HuYaCookieServiceImpl.class);

    private RedisDao redisDao;

    @Autowired
    public HuYaCookieServiceImpl(RedisDao redisDao) {
        this.redisDao = redisDao;
    }


    @Override
    public void setUserCookieInRedis(String username, String password) {
        for (int i = 1; i <= HuYaUtils.ATTEMPT_TIMES; i++) {
            String cookieIntoRedis = HuYaUtils.login(username, password);
            // 如果找到了正确的Cookie
            if (cookieIntoRedis != null && cookieIntoRedis.contains(HuYaUtils.CHECK_LOGIN_STRING)) {
                // Redis中的值将保留六天，配合周三周日刷新，不会失效(后面的计算6天的值，会由编译器进行优化编译)
                redisDao.setKeyValueWithExpireTime("loginCookie:" + username, cookieIntoRedis, 6 * 24 * 60 * 60 * 1000);
                return;
            }
            // 如果没找到，开始重试
            logger.warn("[Cookie Setter] login failed, try " + i + " times");
        }
        // 重试超过失败次数，打印错误日志
        logger.error("[Cookie Setter] attempt " + HuYaUtils.ATTEMPT_TIMES + " times failed");
    }

    @Override
    public String getUserCookieInRedis(String username) {
        return redisDao.getStringValue(HuYaUtils.COOKIE_IN_REDIS);
    }
}
