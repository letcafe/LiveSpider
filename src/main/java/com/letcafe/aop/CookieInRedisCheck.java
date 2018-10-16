package com.letcafe.aop;

import com.letcafe.service.CookieService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.letcafe.util.HuYaUtils.PASSWORD;
import static com.letcafe.util.HuYaUtils.YY_ID;

@Aspect
@Component
public class CookieInRedisCheck {

    private static final Logger logger = LoggerFactory.getLogger(CookieInRedisCheck.class);

    private CookieService cookieService;

    @Autowired
    public CookieInRedisCheck(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    //切点出去所有的关于API Token的API
    @Around("execution(* com.letcafe.controller.huya.*.*(..)) && @annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void checkCookieInRedis(ProceedingJoinPoint point) throws Throwable {
        String cookie = cookieService.getUserCookieInRedis(YY_ID);
        if (cookie == null) {
            logger.warn("cookie in redis is null, new cookie has been stored");
            cookieService.setUserCookieInRedis(YY_ID, PASSWORD);
            logger.info("Cookie check:false and new cookie = {}", cookieService.getUserCookieInRedis(YY_ID));
        }
        point.proceed();
    }
}
