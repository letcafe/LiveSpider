package com.letcafe.controller.huya;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.mongo.LiveInfoLog;
import com.letcafe.service.HuYaGameTypeService;
import com.letcafe.service.HuYaLiveInfoService;
import com.letcafe.service.LiveInfoLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/getter/huya/liveInfo")
public class LiveInfoGetter {

    private final Logger logger = LoggerFactory.getLogger(LiveInfoGetter.class);

    private HuYaGameTypeService huYaGameTypeService;
    private HuYaLiveInfoService huYaLiveInfoService;
    private LiveInfoLogService liveInfoLogService;

    public LiveInfoGetter() {
    }

    @Autowired
    public LiveInfoGetter(HuYaGameTypeService huYaGameTypeService,
                          HuYaLiveInfoService huYaLiveInfoService,
                          LiveInfoLogService liveInfoLogService) {
        this.huYaGameTypeService = huYaGameTypeService;
        this.huYaLiveInfoService = huYaLiveInfoService;
        this.liveInfoLogService = liveInfoLogService;
    }

    public List<HuYaLiveInfo> listHuYaLiveByGid(int gid){
        RestTemplate restTemplate = new RestTemplate();
        List<HuYaLiveInfo> huYaLiveInfoList = new ArrayList<>(20);
        //body
        Map<String, Object> requestBody = new HashMap<>(3);
        requestBody.put("m", "Live");
        requestBody.put("do", "getProfileRecommendList");
        requestBody.put("gid", gid);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://www.huya.com/cache10min.php?m={m}&do={do}&gid={gid}", String.class, requestBody);
        System.out.println("[responseEntity.getBody()] = " + responseEntity.getBody());
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i ++) {
                HuYaLiveInfo huYaLiveInfo = jsonArray.getObject(i, HuYaLiveInfo.class);
                huYaLiveInfoList.add(huYaLiveInfo);
            }
            return huYaLiveInfoList;
        } else {
            return null;
        }
    }


    private void addHuYaLiveInfoLog(int gid, long logCurrentTime) {
        RestTemplate restTemplate = new RestTemplate();
        //body
        Map<String, Object> requestBody = new HashMap<>(3);
        requestBody.put("m", "Live");
        requestBody.put("do", "getProfileRecommendList");
        requestBody.put("gid", gid);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://www.huya.com/cache10min.php?m={m}&do={do}&gid={gid}", String.class, requestBody);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i ++) {
                HuYaLiveInfo huya = jsonArray.getObject(i, HuYaLiveInfo.class);
                LiveInfoLog liveInfoLog = new LiveInfoLog(
                        huya.getUid(),
                        huya.getSex(),
                        huya.getActivityCount(),
                        huya.getNick(),
                        "" + huya.getGid(),
                        huya.getAttendeeCount(),
                        huya.getLiveHost(),
                        huya.getGame(),
                        new Timestamp(logCurrentTime));
                liveInfoLogService.save(liveInfoLog);
            }
        } else {
            logger.warn(responseEntity.getStatusCode().getReasonPhrase());
        }
    }

    // 更新数据库中主播的直播间详细信息
    @Scheduled(cron = "${huya.task.status.time.saveOrUpdateLiveInfo}")
    public void saveOrUpdateLiveInfo() {
        long startTime = System.currentTimeMillis();
        List<Integer> gidList = huYaGameTypeService.listAllGid();
        for (Integer gid : gidList) {
            List<HuYaLiveInfo> liveList = listHuYaLiveByGid(gid);
            for (HuYaLiveInfo liveInfo : liveList) {
                huYaLiveInfoService.saveOrUpdate(liveInfo);
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("[SaveOrUpdate Live Info] cost time = {}s", (endTime - startTime) / 1000);
    }

    // 将LOL游戏的直播日志存放于MongoDB中
    @Scheduled(cron = "${huya.task.status.time.saveLiveLogs}")
    public void saveLiveLogs() {
        int addGameId = 1;
        long logCurrentTime = Long.valueOf(System.currentTimeMillis() / 1000 + "000");
        addHuYaLiveInfoLog(addGameId, logCurrentTime);
        logger.info("[Log Time] = {};[Total MongoDB Count] = {}", new Timestamp(logCurrentTime).toLocalDateTime(), liveInfoLogService.countAll());
    }

}
