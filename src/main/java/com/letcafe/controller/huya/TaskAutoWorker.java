package com.letcafe.controller.huya;


import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.service.CookieService;
import com.letcafe.service.WebDriverService;
import com.letcafe.util.HttpUtils;
import lombok.Data;
import lombok.Getter;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

    // do watch live work:watch 10 live one day
    @Scheduled(cron = "0 0 3 * * *")
    public void watchNumberedLive() throws IOException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("web driver is null");
            return;
        }
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveList(1);
        try {
            // use 2 more (10 < 10 + 2) for some accident url
            for (int i = 0; i < 12; i ++) {
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

    // watch to get 6 treasure one day
    @Scheduled(cron = "0 5 6 * * *")
    public void watchLiveGetSixTreasure() throws IOException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("web driver is null");
            return;
        }
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveList(1);
        String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
        try {
            webDriver.get(watchUrl);
            Thread.sleep(60 * 60 * 1000);
            logger.info("[Six Treasure:watch 60 minutes to get 6 treasure] watch url = " + watchUrl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
        logger.info("webdirver quit");
    }

    @Scheduled(cron = "0 10 3 * * *")
    public void sendPubMessage() throws IOException, InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("web driver is null");
            return;
        }
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveList(1);
        String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
        String message = "6666翻了";
        webDriver.get(watchUrl);
        WebElement chatInput = webDriver.findElement(By.id("pub_msg_input"));
        chatInput.click();
        Thread.sleep(2 * 1000);
        chatInput.sendKeys(message);
        WebElement chatSendButton = webDriver.findElement(By.id("msg_send_bt"));
        Thread.sleep(2 * 1000);
        chatSendButton.click();
        Thread.sleep(2 * 1000);
        logger.info("[send message to live(" + watchUrl +")] = " + message);
        webDriver.quit();
        logger.info("webdirver quit");
    }

    @Scheduled(cron = "0 15 3 * * *")
    public void subscribeOneLiveRoomTask() throws IOException, InterruptedException {
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take DNF for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveList(2);
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

    private JSONObject subscribeLiveRoom(long subscribeUid) throws IOException, InterruptedException {
        String loginCookie = cookieService.getUserCookieInRedis(YY_ID);
        if (loginCookie == null) {
            logger.error("cookie in redis is null or redis down");
            return null;
        }
        long subscribeTime = System.currentTimeMillis();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("cookie", loginCookie);
        String url="https://www.huya.com/member/index.php?m=Activity&do=jsonpSubU&from=act&uid=" + subscribeUid + "&callback=jQuery11110531477879956588_" + subscribeTime + "&type=Subscribe&_=" + subscribeTime;
        HttpResponse response = HttpUtils.doGet(url, headerMap);
        int StatusCode = response.getStatusLine().getStatusCode();
        String entity;
        if(StatusCode == 200){
            logger.info("[subscribe task] url = " + url);
            entity = EntityUtils.toString (response.getEntity(),"utf-8");
            JSONObject jsonObject = new JSONObject(entity.substring(entity.indexOf('{'), entity.lastIndexOf('}') + 1));
            logger.info("subscribe JSON = " + jsonObject);
            return jsonObject;
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
            return null;
        }
    }

    private JSONObject unSubscribeLiveRoom(long subscribeUid) throws IOException, InterruptedException {
        String loginCookie = cookieService.getUserCookieInRedis(YY_ID);
        if (loginCookie == null) {
            logger.error("cookie in redis is null or redis down");
            return null;
        }
        long subscribeTime = System.currentTimeMillis();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("cookie", loginCookie);
        String url="https://www.huya.com/member/index.php?m=Activity&do=jsonpSubU&from=act&uid=" + subscribeUid + "&callback=jQuery1111048374755901934874_" + subscribeTime + "&type=Cancel&_=" + subscribeTime;
        HttpResponse response = HttpUtils.doGet(url, headerMap);
        int StatusCode = response.getStatusLine().getStatusCode();
        String entity;
        if(StatusCode == 200){
            logger.info("[unsubscribe task] url = " + url);
            entity = EntityUtils.toString (response.getEntity(),"utf-8");
            JSONObject jsonObject = new JSONObject(entity.substring(entity.indexOf('{'), entity.lastIndexOf('}') + 1));
            logger.info("unsubscribe json = " + jsonObject);
            return jsonObject;
        }else {
            EntityUtils.consume(response.getEntity());
            return null;
        }
    }
}
