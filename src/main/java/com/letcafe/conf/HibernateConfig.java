package com.letcafe.conf;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig implements TransactionManagementConfigurer{
    @Autowired
    private SessionFactory sessionFactory;

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setPackagesToScan("com.letcafe.bean");
        Properties props = new Properties();
//            props.setProperty("hibernate.hbm2ddl.auto", "update");//开启Hibernate自动根据Bean创建数据表结构
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        localSessionFactoryBean.setHibernateProperties(props);
        return  localSessionFactoryBean;
    }

    public PlatformTransactionManager annotationDrivenTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

}
