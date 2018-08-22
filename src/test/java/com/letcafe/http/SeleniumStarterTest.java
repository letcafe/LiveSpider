package com.letcafe.http;

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
    public void testHuYaLogin() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        // start chrome without GUI
//        options.addArguments("--headless");

        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        WebDriver webDriver = new ChromeDriver(chromeDriverService, options);
        webDriver.manage().window().maximize();
        webDriver.get("https://i.huya.com/");

        // set huya login iframe and switch to it,then wait time to get its login form
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 60, 500);
        loginFrameWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("udbsdk_frm_normal"));
        logger.info("set huya login iframe and switch to it,then wait time to get its login form");

        WebElement usernameInput = webDriver.findElement(By.className("E_acct"));
        WebElement passwordInput = webDriver.findElement(By.className("E_passwd"));
        WebElement loginBtn = webDriver.findElement(By.cssSelector("#m_commonLogin .form_item .E_login"));

        // please set your own yyid and password
        usernameInput.sendKeys("1656777876");
        passwordInput.sendKeys("*");
        logger.info("username and password has been set");

        loginBtn.click();
        logger.info("login btn has been clicked");

//        webDriver.switchTo().defaultContent();
        WebDriverWait switchToPersonalPageWait = new WebDriverWait(webDriver, 10, 500);
        switchToPersonalPageWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".icon-filter li"), 0));
        System.out.println("ahaha");
        webDriver.get("https://www.huya.com/member/task.php?m=User&do=listTotal&callback=huyaNavUserCard");
        WebElement webElement = webDriver.findElement(By.xpath("/html/body"));
        logger.info("userInformationText = " + webElement.getText());
    }
}
