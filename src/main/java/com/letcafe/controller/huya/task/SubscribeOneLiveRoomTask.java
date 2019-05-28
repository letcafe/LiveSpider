package com.letcafe.controller.huya.task;

import com.alibaba.fastjson.JSONObject;
import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaProperties;
import com.letcafe.controller.huya.LiveInfoGetter;
import com.letcafe.service.CookieService;
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

@Controller
public class SubscribeOneLiveRoomTask {

    private static final Logger logger = LoggerFactory.getLogger(SubscribeOneLiveRoomTask.class);

    private CookieService cookieService;
    private HuYaProperties huYaProperties;

    @Autowired
    public SubscribeOneLiveRoomTask(CookieService cookieService, HuYaProperties huYaProperties) {
        this.cookieService = cookieService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 完成订阅直播间任务
     *
     * @throws InterruptedException sleep中断异常
     */
    @Scheduled(cron = "${huya.task.worker.time.subscribeOneLiveRoomTask}")
    public void subscribeOneLiveRoomTask() throws InterruptedException {
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take DNF for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(2);
        // need to ensure the one you order and disorder is the same
        Long subscribeUid = liveInfoList.get(0).getUid();
        JSONObject subObj = subAndUnSub(subscribeUid, true);
        TimeUnit.SECONDS.sleep(10);
        JSONObject unSubObj = subAndUnSub(subscribeUid, false);
        if (subObj != null && unSubObj != null) {
            if (subObj.getInteger("status") == 1 && unSubObj.getInteger("status") == 3) {
                logger.info("[Subscribe One Live] task successfully done");
            } else {
                logger.error("[Subscribe One Live] failed,status code not equal the standard");
            }
        } else {
            logger.error("[Subscribe One Live] failed,return json object is/are null");
        }
    }


    private JSONObject subAndUnSub(long subscribeUid, boolean isSub) {
        String subType = isSub ? "Subscribe" : "Cancel";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        String loginCookie = cookieService.getUserCookieInRedis(huYaProperties.getYyId());
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
        requestBody.put("type", subType);
        requestBody.put("_", subscribeTime);
        HttpEntity<Map> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        String url = "https://www.huya.com/member/index.php?m={m}&do={do}&from={from}&uid={uid}&callback={callback}&type={type}&_={_}";
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, requestBody);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            String responseString = responseEntity.getBody();
            JSONObject jsonObject = JSONObject.parseObject(responseString.substring(responseString.indexOf('{'), responseString.lastIndexOf('}') + 1));
            if (isSub) {
                logger.info("[Subscribe One Live] JSON = {}", jsonObject);
            } else {
                logger.info("[UnSubscribe One Live] JSON = {}", jsonObject);
            }
            return jsonObject;
        } else {
            return null;
        }
    }
}
