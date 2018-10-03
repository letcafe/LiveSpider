package com.letcafe.dao.impl;

import com.letcafe.bean.HuYaTask;
import com.letcafe.dao.HuYaTaskDao;
import com.letcafe.service.HuYaTaskService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HuYaTasksDaoImpl implements HuYaTaskDao {

    private SessionFactory sessionFactory;

    @Autowired
    public HuYaTasksDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(HuYaTask huYaTask) {
        currentSession().save(huYaTask);
    }
}
