package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.service.CookieService;
import com.letcafe.service.HuYaUserLevelService;
import com.letcafe.util.JacksonUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.letcafe.util.HuYaUtils.YY_ID;

@Controller
public class UserLevelAndTaskGetter {

    private final Logger logger = LoggerFactory.getLogger(UserLevelAndTaskGetter.class);

    private HuYaUserLevelService huYaUserLevelService;
    private CookieService cookieService;

    @Autowired
    public UserLevelAndTaskGetter(HuYaUserLevelService huYaUserLevelService, CookieService cookieService) {
        this.huYaUserLevelService = huYaUserLevelService;
        this.cookieService = cookieService;
    }


    @Scheduled(cron = "0 0 0/4 * * *")
    public void setUserTaskStatus() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("cookie", cookieService.getUserCookieInRedis(YY_ID));
        //body
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("m", "User");
        requestBody.add("do", "listTotal");
        requestBody.add("callback", "huyaNavUserCard");
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://www.huya.com/member/task.php", requestEntity, String.class);

        String entity = "";
        //如果状态响应码为200，则获取html实体内容或者json文件
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            //EntityUtils.toString (responseEntity,"utf-8")
            entity = responseEntity.getBody();
            entity = entity.substring(entity.indexOf("{"), entity.lastIndexOf("}") + 1);
            JSONObject rawJson = new JSONObject(entity);

            JSONObject huyaNavUserCard = rawJson.getJSONObject("data");
            JSONObject userLevel = huyaNavUserCard.getJSONObject("level");

            //parse json data, and get its data-level, then make it into obj
            String levelData = userLevel.toString();
            HuYaUserLevel huYaUserLevel = JacksonUtils.readValue(levelData, HuYaUserLevel.class);
            logger.info("[Task Finish Status : MySQL] add number = {}", entity.length());
            if(huYaUserLevel != null) {
                huYaUserLevel.setYyId(YY_ID);
                huYaUserLevelService.save(huYaUserLevel);
            } else {
                logger.error("[Task Finish Status : MySQL] huYaUserLevel object is null");
            }
        } else {
            logger.warn("[Http Entity] = " + entity);
        }
    }

}
