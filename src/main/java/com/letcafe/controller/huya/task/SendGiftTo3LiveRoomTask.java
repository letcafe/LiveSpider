package com.letcafe.controller.huya.task;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaProperties;
import com.letcafe.controller.huya.LiveInfoGetter;
import com.letcafe.service.WebDriverService;
import org.openqa.selenium.By;
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
public class SendGiftTo3LiveRoomTask {

    private static final Logger logger = LoggerFactory.getLogger(SendGiftTo3LiveRoomTask.class);

    private WebDriverService webDriverService;
    private HuYaProperties huYaProperties;

    @Autowired
    public SendGiftTo3LiveRoomTask(WebDriverService webDriverService, HuYaProperties huYaProperties) {
        this.webDriverService = webDriverService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 完成给三个主播送礼物的任务 + 给自己订阅的主播送7个虎粮
     *
     * @throws InterruptedException 线程被打断
     */
    @Scheduled(cron = "${huya.task.worker.time.sendGiftTo3LiveRoom}")
    public void sendGiftTo3LiveRoom() throws InterruptedException {
        // 虎粮代表4
        Integer giftId = 4;
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
        StringBuilder liveRoomIds = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            String liveRoomStr = "https://www.huya.com/" + liveInfoList.get(i).getProfileRoom();
            sendGiftToTargetLiveRoom(liveRoomStr, giftId, 1);
            liveRoomIds.append(liveInfoList.get(i).getProfileRoom()).append(",");
        }
        sendGiftToTargetLiveRoom("https://www.huya.com/941103", giftId, 7);
        logger.info("[Send Gift : To 3 Live] roomId = {}", liveRoomIds.deleteCharAt(liveRoomIds.length() - 1));
    }

    private void sendGiftToTargetLiveRoom(String liveRoomStr, Integer giftId, Integer sendGiftNumber) throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(huYaProperties.getYyId());
        if (webDriver == null) {
            logger.error("[System] WebDriver is null");
            return;
        }
        webDriver.get(liveRoomStr);
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);
        loginFrameWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#player-face ul li[propsid]")));
        List<WebElement> giftButtonList = webDriver.findElements(By.cssSelector("#player-face ul li[propsid]"));

        // 从众多的礼物中选择出目标礼物，送给该名主播
        for (WebElement targetSendGift : giftButtonList) {
            // 如果礼物ID = 目标ID
            if (targetSendGift.getAttribute("propsid").equals("" + giftId)) {
                // 第一次送礼物时一般会需要确认，送 1 个
                targetSendGift.click();
                // 点击确认发送礼物按钮
                loginFrameWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#player-gift-dialog .confirm")));
                WebElement ensureSendButton = webDriver.findElement(By.cssSelector("#player-gift-dialog .confirm"));
                loginFrameWait.until(ExpectedConditions.elementToBeClickable(ensureSendButton));
                ensureSendButton.click();
                TimeUnit.SECONDS.sleep(2);
                // 当第一次送完礼物后就不在需要确认，直接点击即赠送礼物，送 n-1 个
                for (int i = 0; i < sendGiftNumber - 1; i++) {
                    // 点击该礼物
                    targetSendGift.click();
                    // 等待，以让请求加载完毕
                    TimeUnit.SECONDS.sleep(1);
                    logger.info("[giftId = " + giftId + "] has been sent to -> " + liveRoomStr + ", number = " + sendGiftNumber);
                }
            }
        }
        logger.info("[giftId = " + giftId + "] has been sent to -> " + liveRoomStr + ", number = " + sendGiftNumber);
    }
}
