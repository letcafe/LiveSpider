package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.service.HuYaUserLevelService;
import com.letcafe.util.HuYaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class UserLevelAndTaskGetter {

    private final Logger logger = LoggerFactory.getLogger(UserLevelAndTaskGetter.class);

    private HuYaUserLevelService huYaUserLevelService;

    // init my login param
    private static final String yyId = "1656777876";
    private static final String password = "***";
    private static final String cookieRedisKey = "loginCookie_" + yyId;

    @Autowired
    public UserLevelAndTaskGetter(HuYaUserLevelService huYaUserLevelService) {
        this.huYaUserLevelService = huYaUserLevelService;
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void setUserLevel() {

        // simulate login and get user task json
        String userLoginCookie = HuYaUtils.getLoginCookie(yyId, password);
        huYaUserLevelService.saveLoginCookie(cookieRedisKey, userLoginCookie);
        System.out.println("[insert userLoginCookie] = " + userLoginCookie);
        System.out.println("[get form redis userLoginCookie] = " + huYaUserLevelService.getLoginCookie(cookieRedisKey));
//        JSONObject userLevel = new JSONObject(userLevelResult);

        // parse json data, and get its data-level, then make it into obj
//        String levelData = userLevel.getJSONObject("data").get("level").toString();
//        HuYaUserLevel huYaUserLevel = JacksonUtil.readValue(levelData, HuYaUserLevel.class);
//        if(huYaUserLevel != null) {
//            huYaUserLevel.setYyId(yyId);
//            huYaUserLevelService.save(huYaUserLevel);
//        } else {
//            logger.error("huYaUserLevel object is null, so called this error");
//        }
    }
}
