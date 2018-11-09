package com.letcafe.dao.jdbc.impl;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.dao.jdbc.HuYaLiveInfoJdbc;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class HuYaLiveInfoJdbcImpl implements HuYaLiveInfoJdbc {
    private SessionFactory sessionFactory;

    @Autowired
    public HuYaLiveInfoJdbcImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(HuYaLiveInfo huYaLiveInfo) {
        currentSession().saveOrUpdate(huYaLiveInfo);
    }

}
