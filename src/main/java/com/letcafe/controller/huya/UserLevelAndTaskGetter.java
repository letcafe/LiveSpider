package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.service.HuYaUserLevelService;
import com.letcafe.util.HttpUtils;
import com.letcafe.util.HuYaUtils;
import com.letcafe.util.JacksonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class UserLevelAndTaskGetter {

    private final Logger logger = LoggerFactory.getLogger(UserLevelAndTaskGetter.class);

    private HuYaUserLevelService huYaUserLevelService;

    @Autowired
    public UserLevelAndTaskGetter(HuYaUserLevelService huYaUserLevelService) {
        this.huYaUserLevelService = huYaUserLevelService;
    }


    // every Sunday and Wednesday update cookie in redis
    @Scheduled(cron = "0 0 0 ? * SUN,WED")
    public void setUserLoginCookie() {
        String cookieInRedis = huYaUserLevelService.getLoginCookie(HuYaUtils.COOKIE_IN_REDIS);
        while (cookieInRedis == null) {
            // simulate login and get user task json
            Set<Cookie> cookies = huYaUserLevelService.getAllLoginCookie();
            cookieInRedis = HuYaUtils.cookieToString(cookies);
            huYaUserLevelService.saveLoginCookie(HuYaUtils.COOKIE_IN_REDIS, cookieInRedis);
        }
        logger.info("[cookie in redis value] = " + cookieInRedis);
    }

    @Scheduled(cron = "0 0 0/4 * * *")
    public void setUserTaskStatus() throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("cookie", getUserLoginCookie());
        HttpResponse response = HttpUtils.doGet("https://www.huya.com/member/task.php?m=User&do=listTotal&callback=huyaNavUserCard", headerMap);

        int StatusCode = response.getStatusLine().getStatusCode();
        String entity = "";
        //如果状态响应码为200，则获取html实体内容或者json文件
        if(StatusCode == 200){
            entity = EntityUtils.toString (response.getEntity(),"utf-8");
            entity = entity.substring(entity.indexOf("{"), entity.lastIndexOf("}") + 1);
            JSONObject rawJson = new JSONObject(entity);

            JSONObject huyaNavUserCard = rawJson.getJSONObject("data");
            JSONObject userLevel = huyaNavUserCard.getJSONObject("level");

            //parse json data, and get its data-level, then make it into obj
            String levelData = userLevel.toString();
            HuYaUserLevel huYaUserLevel = JacksonUtil.readValue(levelData, HuYaUserLevel.class);
            logger.info("set user task info to mysql,entity.length() = " + entity.length());
            if(huYaUserLevel != null) {
                huYaUserLevel.setYyId(HuYaUtils.YY_ID);
                huYaUserLevelService.save(huYaUserLevel);
            } else {
                logger.error("huYaUserLevel object is null, so called this error");
            }
        }else {
            EntityUtils.consume(response.getEntity());
        }
    }

    public String getUserLoginCookie() {
        setUserLoginCookie();
        return huYaUserLevelService.getLoginCookie(HuYaUtils.COOKIE_IN_REDIS);
    }
}
