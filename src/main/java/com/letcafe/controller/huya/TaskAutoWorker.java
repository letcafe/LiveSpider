package com.letcafe.controller.huya;


import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.service.CookieService;
import com.letcafe.service.WebDriverService;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.letcafe.util.HuYaUtils.COOKIE_IN_REDIS;
import static com.letcafe.util.HuYaUtils.YY_ID;

@Controller
public class TaskAutoWorker {

    private static final Logger logger = LoggerFactory.getLogger(TaskAutoWorker.class);

    private WebDriverService webDriverService;
    private CookieService cookieService;

    @Autowired
    public TaskAutoWorker(WebDriverService webDriverService, CookieService cookieService) {
        this.webDriverService = webDriverService;
        this.cookieService = cookieService;
    }

    // 完成每天观看10名主播任务
    @Scheduled(cron = "0 0 3 * * *")
    public void watchNumberedLive() {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("[System] WebDriver is null");
            return;
        }
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
        try {
            // use 2 more (10 < 10 + 2) for some accident url
            for (int i = 0; i < 12; i++) {
                String watchUrl = "https://www.huya.com/" + liveInfoList.get(i).getProfileRoom();
                webDriver.get(watchUrl);
                TimeUnit.SECONDS.sleep(5);
                logger.info("[Task:watch 10 live one day] No.{} watch url = {}", i + 1,  watchUrl);
            }
            logger.info("[Watch 10 Live] end");
        } catch (Exception ex) {
            logger.error("[Watch 10 Live] fail");
            ex.printStackTrace(System.out);
        } finally {
            webDriver.quit();
            logger.info("[System] WebDriver quit");
        }
    }

    // 完成弹幕发送任务
    @Scheduled(cron = "${huya.task.worker.time.sendPubMessage}")
    public void sendPubMessage() throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("[System] WebDriver is null");
            return;
        }
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);

        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
        String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
        String message = "6666";
        webDriver.get(watchUrl);
        TimeUnit.SECONDS.sleep(3);
        WebElement chatInput = webDriver.findElement(By.id("pub_msg_input"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        // 添加Enable的class使得发送按钮可以直接点击
        js.executeScript("document.getElementById('msg_send_bt').className += 'enable';");
        chatInput.sendKeys(message);
        WebElement chatSendButton = webDriver.findElement(By.id("msg_send_bt"));
        loginFrameWait.until(ExpectedConditions.elementToBeClickable(chatSendButton));
        chatSendButton.click();
        TimeUnit.SECONDS.sleep(1);
        logger.info("[send message to live(" + watchUrl + ")] = " + message);
        webDriver.quit();
        logger.info("[System] WebDriver quit");
    }

    // 完成订阅直播间任务
    @Scheduled(cron = "${huya.task.worker.time.subscribeOneLiveRoomTask}")
    public void subscribeOneLiveRoomTask() throws InterruptedException {
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take DNF for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(2);
        // need to ensure the one you order and disorder is the same
        Long subscribeUid = liveInfoList.get(0).getUid();
        JSONObject subObj = subscribeLiveRoom(subscribeUid);
        TimeUnit.SECONDS.sleep(10);
        JSONObject unSubObj = unSubscribeLiveRoom(subscribeUid);
        if (subObj != null && unSubObj != null) {
            if (subObj.getInt("status") == 1 && unSubObj.getInt("status") == 3) {
                logger.info("[Subscribe One Live] task successfully done");
            } else {
                logger.error("[Subscribe One Live] failed,status code not equal the standard");
            }
        } else {
            logger.error("[Subscribe One Live] failed,return json object is/are null");
        }
    }

    // 完成给三个主播送礼物的任务 + 给自己订阅的主播送7个虎粮
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


    // 完成每天观看一小时，获取六个宝箱任务
    @Scheduled(cron = "${huya.task.worker.time.watchLiveGetSixTreasure}")
    public void watchLiveGetSixTreasure() throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("[System] WebDriver is null");
            return;
        }
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
        webDriver.quit();
        logger.info("[System] WebDriver quit");
    }

    // 根据给定的webDriver,领取所有的宝箱奖励
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
                successGetPrizeNumber ++;
                logger.info("[Six Treasure : Receive Detail] item {}", numberPrizeLabel.getText());
            }
        }
        logger.info("[Six Treasure : Receive Report] success = {}, total = {}", successGetPrizeNumber, treasureBoxList.size());
    }

    private void sendGiftToTargetLiveRoom(String liveRoomStr, Integer giftId, Integer sendGiftNumber) throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
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
        webDriver.quit();
        logger.info("[System] WebDriver quit");
    }

    private JSONObject subscribeLiveRoom(long subscribeUid) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        String loginCookie = cookieService.getUserCookieInRedis(YY_ID);
        if (loginCookie == null) {
            logger.error("cookie in redis is null or redis down");
            return null;
        }
        requestHeaders.add("cookie", loginCookie);
        //body
        long subscribeTime = System.currentTimeMillis();
        Map<String, Object> requestBody = new HashMap<>(3);
        requestBody.put("m", "Activity");
        requestBody.put("do", "jsonpSubU");
        requestBody.put("from", "act");
        requestBody.put("uid", subscribeUid);
        requestBody.put("callback", "jQuery11110531477879956588_" + subscribeTime);
        requestBody.put("type", "Subscribe");
        requestBody.put("_", subscribeTime);
        HttpEntity<Map> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        String url = "https://www.huya.com/member/index.php?m={m}&do={do}&from={from}&uid={uid}&callback={callback}&type={type}&_={_}";
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, requestBody);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            String responseString = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(responseString.substring(responseString.indexOf('{'), responseString.lastIndexOf('}') + 1));
            logger.info("[Subscribe One Live] JSON = {}", jsonObject);
            return jsonObject;
        } else {
            return null;
        }
    }

    private JSONObject unSubscribeLiveRoom(long subscribeUid) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        String loginCookie = cookieService.getUserCookieInRedis(YY_ID);
        if (loginCookie == null) {
            logger.error("cookie in redis is null or redis down");
            return null;
        }
        requestHeaders.add("cookie", loginCookie);
        //body
        long subscribeTime = System.currentTimeMillis();
        Map<String, Object> requestBody = new HashMap<>(3);
        requestBody.put("m", "Activity");
        requestBody.put("do", "jsonpSubU");
        requestBody.put("from", "act");
        requestBody.put("uid", subscribeUid);
        requestBody.put("callback", "jQuery11110531477879956588_" + subscribeTime);
        requestBody.put("type", "Cancel");
        requestBody.put("_", subscribeTime);
        HttpEntity<Map> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        String url = "https://www.huya.com/member/index.php?m={m}&do={do}&from={from}&uid={uid}&callback={callback}&type={type}&_={_}";
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, requestBody);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            String responseString = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(responseString.substring(responseString.indexOf('{'), responseString.lastIndexOf('}') + 1));
            logger.info("[UnSubscribe One Live] JSON = {}", jsonObject);
            return jsonObject;
        } else {
            return null;
        }
    }
}
