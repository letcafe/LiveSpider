package com.letcafe.dao.impl;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.dao.HuYaUserLevelDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HuYaUserLevelDaoImpl implements HuYaUserLevelDao {
    private SessionFactory sessionFactory;

    @Autowired
    public HuYaUserLevelDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(HuYaUserLevel huYaUserLevel) {
        currentSession().save(huYaUserLevel);
    }
}
