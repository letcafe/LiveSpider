package com.letcafe.dao.jdbc.impl;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.dao.HuYaGameTypeDao;
import com.letcafe.dao.jdbc.HuYaGameTypeJdbc;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class HuYaGameTypeJdbcImpl implements HuYaGameTypeJdbc {

    private SessionFactory sessionFactory;

    @Autowired
    public HuYaGameTypeJdbcImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(HuYaGameType huYaGameType) {
        currentSession().saveOrUpdate(huYaGameType);
    }

}
