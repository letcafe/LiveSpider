package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.mongo.LiveInfoLog;
import com.letcafe.service.HuYaGameTypeService;
import com.letcafe.service.HuYaLiveInfoService;
import com.letcafe.service.LiveInfoLogService;
import com.letcafe.util.HttpUtils;
import com.letcafe.util.JacksonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    public List<HuYaLiveInfo> listHuYaLiveList(int gid) throws IOException {
        List<HuYaLiveInfo> huYaLiveInfoList = new ArrayList<>(20);
        //初始化一个httpclient
        HttpClient client = HttpClients.createDefault();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        String url="https://www.huya.com/cache10min.php?m=Live&do=getProfileRecommendList&gid=" + gid;
        //抓取的数据
        HttpResponse response = HttpUtils.getRawHtml(client, url);
        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        String entity = "";
        //如果状态响应码为200，则获取html实体内容或者json文件
        if(StatusCode == 200){
            entity = EntityUtils.toString (response.getEntity(),"utf-8");
            JSONObject jsonObject = new JSONObject(entity);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i ++) {
                HuYaLiveInfo huYaLiveInfo = JacksonUtil.readValue(jsonArray.get(i).toString(), HuYaLiveInfo.class);
                huYaLiveInfoList.add(huYaLiveInfo);
            }
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        return huYaLiveInfoList;
    }

    private void updateHuYaLiveInfoById(int gid) throws Exception {
        List<HuYaLiveInfo> liveList = this.listHuYaLiveList(gid);
        for (HuYaLiveInfo liveInfo : liveList) {
            huYaLiveInfoService.saveOrUpdate(liveInfo);
        }
    }


    private void addHuYaLiveInfoLog(int gid, long logCurrentTime) throws Exception {
        HttpClient client = HttpClients.createDefault();
        String url="https://www.huya.com/cache10min.php?m=Live&do=getProfileRecommendList&gid=" + gid;
        HttpResponse response = HttpUtils.getRawHtml(client, url);
        int StatusCode = response.getStatusLine().getStatusCode();
        String entity = "";
        //如果状态响应码为200，则获取html实体内容或者json文件
        if(StatusCode == 200){
            entity = EntityUtils.toString (response.getEntity(),"utf-8");
            JSONObject jsonObject = new JSONObject(entity);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i ++) {
                HuYaLiveInfo huya = JacksonUtil.readValue(jsonArray.get(i).toString(), HuYaLiveInfo.class);
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
        }else {
            EntityUtils.consume(response.getEntity());
        }
    }

    // Every 30 minutes update all huya live information in MySQL
    @Scheduled(cron = "0 20/30 * * * *")
    public void updateAllHuYaLiveInfo() throws Exception {
        long startTime = System.currentTimeMillis();
        List<Integer> gidList = huYaGameTypeService.listAllGid();
        for (Integer gid : gidList) {
            updateHuYaLiveInfoById(gid);
        }
        long endTime = System.currentTimeMillis();
        logger.info("saveOrUpdate all Live.[cost time] = " + (endTime - startTime) / 1000 + "s");
    }

    // Every 30 minutes update all huya log information in MySQL
    @Scheduled(cron = "0 20/30 * * * *")
    public void insertAll() throws Exception {
        String currentTime = "" + System.currentTimeMillis();
        long logCurrentTime = Long.valueOf(currentTime.substring(0, currentTime.length() - 3) + "000");
        addHuYaLiveInfoLog(1, logCurrentTime);
        logger.info("[log Time] = " + new Timestamp(logCurrentTime).toLocalDateTime() + ";[total mongo count] = " + liveInfoLogService.countAll());
    }

}
