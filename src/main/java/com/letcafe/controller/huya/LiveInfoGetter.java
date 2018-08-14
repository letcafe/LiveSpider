package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.parse.HuYaParser;
import com.letcafe.service.HuYaGameTypeService;
import com.letcafe.util.HttpUtils;
import com.letcafe.util.JacksonUtil;
import com.letcafe.util.UrlFetcher;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/getter/huya/liveInfo")
public class LiveInfoGetter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HuYaGameTypeService huYaGameTypeService;

    @Autowired
    public LiveInfoGetter(HuYaGameTypeService huYaGameTypeService) {
        this.huYaGameTypeService = huYaGameTypeService;
    }

    @Scheduled(fixedRate = 10000)
    public String gameType(/*int gid*/) throws Exception {
        //初始化一个httpclient
        HttpClient client = HttpClients.createDefault();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        String url="https://www.huya.com/cache10min.php?m=Live&do=getProfileRecommendList&gid=1";
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
            System.out.println("[jsonArray.get(0)] = " + jsonArray.get(0));
            HuYaLiveInfo huYaLiveInfo = JacksonUtil.readValue(jsonArray.get(0).toString(), HuYaLiveInfo.class);
            if(huYaLiveInfo != null) {
                System.out.println("[huYaLiveInfo] = " + huYaLiveInfo);
            } else {
                System.out.println("[huYaLiveInfo] = null");
            }
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        return entity;
    }

}
