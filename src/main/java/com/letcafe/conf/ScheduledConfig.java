package com.letcafe.conf;

import com.letcafe.bean.HuYaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class ScheduledConfig {

    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService(HuYaProperties huYaProperties) {
        // configuration for @Scheduled executor number
        return new ScheduledThreadPoolExecutor(huYaProperties.getCpuCore() * 2, Executors.defaultThreadFactory());
    }
}