package com.letcafe.http;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.util.HuYaUtils;
import com.letcafe.util.JacksonUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SeleniumStarterTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() {
        String userLevelResult = HuYaUtils.userDailyTaskInfo("1656777876", "gdy19941231");
        JSONObject userLevel = new JSONObject(userLevelResult);
        String levelData = userLevel.getJSONObject("data").get("level").toString();
        HuYaUserLevel huYaUserLevel = JacksonUtil.readValue(levelData, HuYaUserLevel.class);
        System.out.println("处理后的JSON = " + huYaUserLevel);
    }

}
