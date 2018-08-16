package com.letcafe.dao.impl;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.dao.HuYaGameTypeDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HuYaGameTypeDaoImpl implements HuYaGameTypeDao {

    private static final String listHuYaGameType = "SELECT * FROM huya_game_type";

    private static final String listAllGid = "SELECT gid FROM huya_game_type";

    private SessionFactory sessionFactory;

    @Autowired
    public HuYaGameTypeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
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

    @Override
    public List<HuYaGameType> listHuYaGameType() {
        Query<HuYaGameType> query = currentSession().createNativeQuery(listHuYaGameType, HuYaGameType.class);
        return query.getResultList();
    }

    @Override
    public List<Integer> listAllGid() {
        Query query = currentSession().createNativeQuery(listAllGid);
        return query.getResultList();
    }
}
