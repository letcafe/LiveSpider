package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.bean.mongo.LiveInfoLog;
import com.letcafe.service.HuYaUserLevelService;
import com.letcafe.util.HttpUtils;
import com.letcafe.util.HuYaUtils;
import com.letcafe.util.JacksonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserLevelAndTaskGetter {

    private final Logger logger = LoggerFactory.getLogger(UserLevelAndTaskGetter.class);

    private HuYaUserLevelService huYaUserLevelService;

    // init my login param
    private static final String yyId = "1656777876";
    private static final String password = "gdy19941231";
    private static final String cookieRedisKey = "loginCookie_" + yyId;

    @Autowired
    public UserLevelAndTaskGetter(HuYaUserLevelService huYaUserLevelService) {
        this.huYaUserLevelService = huYaUserLevelService;
    }


    // every Sunday and Wednesday update cookie in redis
    @Scheduled(cron = "0 0 0 ? * SUN,WED")
    public void setUserLoginCookie() {
        String cookieInRedis;
        do {
            // simulate login and get user task json
            String userLoginCookie = HuYaUtils.getLoginCookie(yyId, password);
            huYaUserLevelService.saveLoginCookie(cookieRedisKey, userLoginCookie);
            cookieInRedis = huYaUserLevelService.getLoginCookie(cookieRedisKey);
        } while (cookieInRedis == null);
        logger.info("[cookie in redis value] = " + cookieInRedis);
    }

//    @Scheduled(fixedRate = 60 * 1000)
    @Scheduled(cron = "0 0 * * * *")
    public void setUserTaskStatus() throws IOException {
        String huyaLoginCookie = huYaUserLevelService.getLoginCookie(cookieRedisKey);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("cookie", huyaLoginCookie);
        HttpResponse response = HttpUtils.doGet("https://www.huya.com/member/task.php?m=User&do=listTotal&callback=huyaNavUserCard", headerMap);

        int StatusCode = response.getStatusLine().getStatusCode();
        String entity = "";
        //如果状态响应码为200，则获取html实体内容或者json文件
        if(StatusCode == 200){
            entity = EntityUtils.toString (response.getEntity(),"utf-8");
            entity = entity.substring(entity.indexOf("{"), entity.lastIndexOf("}") + 1);
            logger.info("parse huyaNavUserCard = " + entity);
            JSONObject rawJson = new JSONObject(entity);
            JSONObject huyaNavUserCard = rawJson.getJSONObject("data");
            JSONObject userLevel = huyaNavUserCard.getJSONObject("level");

            //parse json data, and get its data-level, then make it into obj
            String levelData = userLevel.toString();
            HuYaUserLevel huYaUserLevel = JacksonUtil.readValue(levelData, HuYaUserLevel.class);
            if(huYaUserLevel != null) {
                huYaUserLevel.setYyId(yyId);
                huYaUserLevelService.save(huYaUserLevel);
            } else {
                logger.error("huYaUserLevel object is null, so called this error");
            }
        }else {
            EntityUtils.consume(response.getEntity());
        }
    }
}
