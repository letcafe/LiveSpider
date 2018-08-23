package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.service.HuYaLiveInfoService;
import com.letcafe.service.HuYaUserLevelService;
import com.letcafe.util.HuYaUtils;
import com.letcafe.util.JacksonUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class UserLevelAndTaskGetter {

    private final Logger logger = LoggerFactory.getLogger(UserLevelAndTaskGetter.class);

    private HuYaUserLevelService huYaUserLevelService;

    @Autowired
    public UserLevelAndTaskGetter(HuYaUserLevelService huYaUserLevelService) {
        this.huYaUserLevelService = huYaUserLevelService;
    }

    @Scheduled(fixedRate = 30 * 1000)
    public void setUserLevel() {
        // init my login param
        String yyId = "1656777876";
        String password = "***";

        // simulate login and get user task json
        String userLevelResult = HuYaUtils.userDailyTaskInfo(yyId, password);
        JSONObject userLevel = new JSONObject(userLevelResult);

        // parse json data, and get its data-level, then make it into obj
        String levelData = userLevel.getJSONObject("data").get("level").toString();
        HuYaUserLevel huYaUserLevel = JacksonUtil.readValue(levelData, HuYaUserLevel.class);
        if(huYaUserLevel != null) {
            huYaUserLevel.setYyId(yyId);
            huYaUserLevelService.save(huYaUserLevel);
        } else {
            logger.error("huYaUserLevel object is null, so called this error");
        }
    }
}
