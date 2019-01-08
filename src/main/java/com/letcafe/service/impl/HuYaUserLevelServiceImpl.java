package com.letcafe.service.impl;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.dao.HuYaUserLevelDao;
import com.letcafe.dao.RedisDao;
import com.letcafe.service.HuYaUserLevelService;
import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.letcafe.util.HuYaUtils.*;

@ConfigurationProperties(prefix = "huya")
@Service
@Transactional
public class HuYaUserLevelServiceImpl implements HuYaUserLevelService {

    private static final Logger logger = LoggerFactory.getLogger(HuYaUserLevelServiceImpl.class);

    private HuYaUserLevelDao userLevelDao;

    @Autowired
    public HuYaUserLevelServiceImpl(HuYaUserLevelDao userLevelDao) {
        this.userLevelDao = userLevelDao;
    }

    @Override
    public void save(HuYaUserLevel huYaUserLevel) {
        userLevelDao.save(huYaUserLevel);
    }

}
