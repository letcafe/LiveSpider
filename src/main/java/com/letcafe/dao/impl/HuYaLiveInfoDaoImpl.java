package com.letcafe.dao.impl;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.dao.HuYaLiveInfoDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HuYaLiveInfoDaoImpl implements HuYaLiveInfoDao {

    private SessionFactory sessionFactory;

    @Autowired
    public HuYaLiveInfoDaoImpl(SessionFactory sessionFactory) {
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
