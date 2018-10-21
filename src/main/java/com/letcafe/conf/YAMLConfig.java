package com.letcafe.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class YAMLConfig {

    private static final Logger logger = LoggerFactory.getLogger(YAMLConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("huya.yaml"),
                new ClassPathResource("huya-dev.yaml"),
                new ClassPathResource("huya-prod.yaml"),
                new ClassPathResource("huya-tjprod.yaml"));
        if (yaml.getObject() == null) {
            logger.error("your yaml file was not found");
            return null;
        }
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}
