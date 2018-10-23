package com.letcafe.conf;

import com.letcafe.util.HuYaUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class ScheduledConfig {

    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        // configuration for @Scheduled executor number
        return new ScheduledThreadPoolExecutor(HuYaUtils.CPU_CORE * 2);
    }
}
