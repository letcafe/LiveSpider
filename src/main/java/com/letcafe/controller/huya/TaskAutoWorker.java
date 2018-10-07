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
            logger.error("web driver is null");
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
                Thread.sleep(5000);
                logger.info("[Task:watch 10 live one day] No." + (i + 1) + " watch url = " + watchUrl);
            }
            logger.info("[Task:watch 10 live one day] ended");
        } catch (Exception ex) {
            logger.error("watch 10 failed");
            ex.printStackTrace(System.out);
        } finally {
            webDriver.quit();
            logger.info("webdirver quit");
        }
    }

    // 完成弹幕发送任务
    @Scheduled(cron = "0 10 3 * * *")
    public void sendPubMessage() throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("web driver is null");
            return;
        }
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);

        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
        String watchUrl = "https://www.huya.com/11293861";
//        String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
        String message = "6666";
        webDriver.get(watchUrl);
        Thread.sleep(2 * 1000);
        WebElement chatInput = webDriver.findElement(By.id("pub_msg_input"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        // 添加Enable的class使得发送按钮可以直接点击
        js.executeScript("document.getElementById('msg_send_bt').className += 'enable';");
        chatInput.sendKeys(message);
        WebElement chatSendButton = webDriver.findElement(By.id("msg_send_bt"));
        loginFrameWait.until(ExpectedConditions.elementToBeClickable(chatSendButton));
        chatSendButton.click();
        Thread.sleep(1000);
        logger.info("[send message to live(" + watchUrl + ")] = " + message);
        webDriver.quit();
        logger.info("webdirver quit");
    }

    // 完成订阅直播间任务
    @Scheduled(cron = "0 15 3 * * *")
    public void subscribeOneLiveRoomTask() throws InterruptedException {
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take DNF for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(2);
        // need to ensure the one you order and disorder is the same
        Long subscribeUid = liveInfoList.get(0).getUid();
        JSONObject subObj = subscribeLiveRoom(subscribeUid);
        Thread.sleep(10 * 1000);
        JSONObject unSubObj = unSubscribeLiveRoom(subscribeUid);
        if (subObj != null && unSubObj != null) {
            if (subObj.getInt("status") == 1 && unSubObj.getInt("status") == 3) {
                logger.info("[subscribeOneLiveRoom task] success");
            } else {
                logger.error("[subscribeOneLiveRoom task] failed,status code not equal the standard");
            }
        } else {
            logger.error("[subscribeOneLiveRoom task] failed,return json object is/are null");
        }
    }

    // 完成给三个主播送礼物的任务 + 给自己订阅的主播送7个虎粮
    @Scheduled(cron = "0 20 3 * * *")
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
        logger.info("[Send gift To 3 live room successfully] roomId = " + liveRoomIds.deleteCharAt(liveRoomIds.length() - 1));
    }


    // 完成每天观看一小时，获取六个宝箱任务
    @Scheduled(cron = "0 1 6 * * *")
    public void watchLiveGetSixTreasure() throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("web driver is null");
            return;
        }
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
        String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
        webDriver.get(watchUrl);
        logger.info("[Six Treasure:watch 60 minutes to get 6 treasure] watch url = " + watchUrl);
        // 等待一小时达到1小时标准再关闭
        Thread.sleep(60 * 60 * 1000);
        webDriver.quit();
        logger.info("webdirver quit");
    }

    private void sendGiftToTargetLiveRoom(String liveRoomStr, Integer giftId, Integer sendGiftNumber) throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("web driver is null");
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
                Thread.sleep(2 * 1000);
                logger.info("[giftId = " + giftId + "] has been sent to -> " + liveRoomStr + ", number = " + sendGiftNumber);

                // 当第一次送完礼物后就不在需要确认，直接点击即赠送礼物，送 n-1 个
                for (int i = 0; i < sendGiftNumber - 1; i++) {
                    // 点击该礼物
                    targetSendGift.click();
                    // 等待，以让请求加载完毕
                    Thread.sleep(1000);
                    logger.info("[giftId = " + giftId + "] has been sent to -> " + liveRoomStr + ", number = " + sendGiftNumber);
                }
            }
        }
        webDriver.quit();
        logger.info("webdirver quit");
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
            logger.info("subscribe JSON = " + jsonObject);
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
            logger.info("cancel subscribe JSON = " + jsonObject);
            return jsonObject;
        } else {
            return null;
        }
    }
}
