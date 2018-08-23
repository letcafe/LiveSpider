package com.letcafe.util;

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

public class HuYaUtils {

    private static Logger logger = LoggerFactory.getLogger(HuYaUtils.class);

    public static String userDailyTaskInfo(String username, String password){
        System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        // start chrome without GUI
//        options.addArguments("--headless");

        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        WebDriver webDriver = new ChromeDriver(chromeDriverService, options);
        webDriver.manage().window().maximize();
        webDriver.get("https://i.huya.com/");

        // set huya login iframe and switch to it,then wait time to get its login form
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);
        loginFrameWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("udbsdk_frm_normal"));
        logger.info("set huya login iframe and switch to it,then wait time to get its login form");

        WebElement usernameInput = webDriver.findElement(By.className("E_acct"));
        WebElement passwordInput = webDriver.findElement(By.className("E_passwd"));
        WebElement loginBtn = webDriver.findElement(By.cssSelector("#m_commonLogin .form_item .E_login"));

        // please set your own yyid and password
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        logger.info("username and password has been set");

        loginBtn.click();
        logger.info("login btn has been clicked");

        WebDriverWait switchToPersonalPageWait = new WebDriverWait(webDriver, 20, 500);
        switchToPersonalPageWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".icon-filter li"), 0));
        webDriver.get("https://www.huya.com/member/task.php?m=User&do=listTotal&callback=huyaNavUserCard");
        WebElement webElement = webDriver.findElement(By.xpath("/html/body"));
        String dataObjStr = webElement.getText();

        // close chrome window when get json
        webDriver.close();
        return dataObjStr.substring(dataObjStr.indexOf('{'), dataObjStr.lastIndexOf('}') + 1);
    }
}
