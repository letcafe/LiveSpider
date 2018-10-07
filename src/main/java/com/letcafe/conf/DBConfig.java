package com.letcafe.conf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    //DB config
    //MySQL ConnectionFactory
    @Profile("dev")
    @Bean
    public DataSource mysqlDataSourceDevelopment() {
        BasicDataSource ds = new BasicDataSource();
        ds.setMaxIdle(6);
        ds.setMinIdle(2);
        ds.setInitialSize(10);
        ds.setDefaultAutoCommit(true);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/live_spider?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&character_set_server=utf8mb4&&characterEncoding=utf-8");
        ds.setUsername("root");
        ds.setPassword("123456");
        return ds;
    }

    @Profile({"prod", "tjprod"})
    @Bean
    public DataSource mysqlDataSourceProduction() {
        BasicDataSource ds = new BasicDataSource();
        ds.setMaxIdle(6);
        ds.setMinIdle(4);
        ds.setInitialSize(10);
        ds.setDefaultAutoCommit(true);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://letcafe.cn:3306/live_spider?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&character_set_server=utf8mb4&&characterEncoding=utf-8");
        ds.setUsername("dongyuguo");
        ds.setPassword("Gg@123456");
        return ds;
    }


}
