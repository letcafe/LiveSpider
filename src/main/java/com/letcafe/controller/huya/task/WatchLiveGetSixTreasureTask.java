package com.letcafe.controller.huya.task;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaProperties;
import com.letcafe.controller.huya.LiveInfoGetter;
import com.letcafe.service.WebDriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class WatchLiveGetSixTreasureTask {

    private static final Logger logger = LoggerFactory.getLogger(WatchLiveGetSixTreasureTask.class);

    private WebDriverService webDriverService;
    private HuYaProperties huYaProperties;

    @Autowired
    public WatchLiveGetSixTreasureTask(WebDriverService webDriverService, HuYaProperties huYaProperties) {
        this.webDriverService = webDriverService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 完成每天观看一小时，获取六个宝箱任务
     *
     * @throws InterruptedException 线程被打断
     */
    @Scheduled(cron = "${huya.task.worker.time.watchLiveGetSixTreasure}")
    public void watchLiveGetSixTreasure() throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(huYaProperties.getYyId());
        if (webDriver == null) {
            logger.error("[System] WebDriver is null");
            return;
        }
        try {
            LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
            // take LOL for example, get LOL live list, if come across exception,log then recursive
            List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
            String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
            webDriver.get(watchUrl);
            logger.info("[Six Treasure : Watch Live] url = {}", watchUrl);
            // 等待55分钟达到6宝箱解锁条件后后再关闭
            TimeUnit.MINUTES.sleep(55);

            // 解锁6个宝箱
            receiveSixTreasurePrize(webDriver);
        } finally {
            webDriver.quit();
            logger.info("[System] WebDriver quit");
        }

    }

    /**
     * 根据给定的webDriver,领取所有的宝箱奖励
     *
     * @param webDriver web浏览器
     * @throws InterruptedException 线程被打断
     */
    private void receiveSixTreasurePrize(WebDriver webDriver) throws InterruptedException {
        WebDriverWait waitForThisPage = new WebDriverWait(webDriver, 20, 500);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        // 先设置player-box属性display:none -> display:block，使得其先课件（为后面点击宝箱领取做铺垫）
        waitForThisPage.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#player-box")));
        WebElement playBoxPanel = webDriver.findElement(By.cssSelector("#player-box"));

        // 添加Enable的class使得发送按钮可以直接点击
        js.executeScript("arguments[0].style=arguments[1]", playBoxPanel, "display:block;");
        List<WebElement> treasureBoxList = webDriver.findElements(By.cssSelector("#player-box .player-box-list ul li"));
        int successGetPrizeNumber = 0;
        for (WebElement boxPrize : treasureBoxList) {
            // 领取按钮
            WebElement getPrizeLabel = boxPrize.findElement(By.cssSelector(".player-box-stat3"));
            // 如果领取宝箱的按钮已经显示出来，那么点击领取
            if (getPrizeLabel.isDisplayed()) {
                js.executeScript("arguments[0].click()", getPrizeLabel);
                logger.info("[Six Treasure : Auto Receive] index = {}", getPrizeLabel.getAttribute("index"));
            }
        }
        // 等待3秒让服务器完成处理
        TimeUnit.SECONDS.sleep(3);

        // 读取领取数量，并加以打印到日志
        for (WebElement boxPrize : treasureBoxList) {
            // 奖励数量按钮
            WebElement numberPrizeLabel = boxPrize.findElement(By.cssSelector(".player-box-stat4"));
            // 如果领取宝箱的按钮已经显示出来，那么点击领取
            if (numberPrizeLabel.isDisplayed()) {
                successGetPrizeNumber++;
                logger.info("[Six Treasure : Receive Detail] item {}", numberPrizeLabel.getText());
            }
        }
        logger.info("[Six Treasure : Receive Report] success = {}, total = {}", successGetPrizeNumber, treasureBoxList.size());
    }
}
