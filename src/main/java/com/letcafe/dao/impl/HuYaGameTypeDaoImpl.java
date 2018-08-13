package com.letcafe.dao.impl;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.dao.HuYaGameTypeDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HuYaGameTypeDaoImpl implements HuYaGameTypeDao {

    @Autowired
    private SessionFactory sessionFactory;

    Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(HuYaGameType huYaGameType) {
        currentSession().save(huYaGameType);
    }

    @Override
    public void saveOrUpdate(HuYaGameType huYaGameType) {
        currentSession().saveOrUpdate(huYaGameType);
    }
}
