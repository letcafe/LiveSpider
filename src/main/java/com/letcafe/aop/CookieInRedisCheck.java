package com.letcafe.aop;

import com.letcafe.bean.HuYaProperties;
import com.letcafe.service.CookieService;
import com.letcafe.util.HuYaUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class CookieInRedisCheck {

    private static final Logger logger = LoggerFactory.getLogger(CookieInRedisCheck.class);

    private CookieService cookieService;
    private HuYaProperties huYaProperties;

    @Autowired
    public CookieInRedisCheck(CookieService cookieService, HuYaProperties huYaProperties) {
        this.cookieService = cookieService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 切点出去所有的关于API Token的API
     */
    @Around("execution(* com.letcafe.controller.huya.*.*(..)) " +
            "&& @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void checkCookieInRedis(ProceedingJoinPoint point) throws Throwable {
        String cookie = cookieService.getUserCookieInRedis(huYaProperties.getYyId());
        if (cookie == null) {
            logger.warn("[Cookie Check : Redis] cookie is null, new cookie will be stored");
            cookieService.setUserCookieInRedis(huYaProperties.getYyId(), huYaProperties.getPassword());
            logger.info("[Cookie Check : Redis] new cookie = {}", cookieService.getUserCookieInRedis(huYaProperties.getYyId()));
        }
        point.proceed();
    }
}
