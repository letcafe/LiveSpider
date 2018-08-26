package com.letcafe.dao.impl;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.dao.HuYaLiveInfoDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HuYaLiveInfoDaoImpl implements HuYaLiveInfoDao {
    private SessionFactory sessionFactory;
    private static final String listNumberedOrderByLiveTimeDesc = "SELECT * FROM huya_live_info ORDER BY update_time DESC LIMIT :number";

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

    @Override
    public List<HuYaLiveInfo> listNumberedOrderByLiveTimeDesc(int number) {
        Query<HuYaLiveInfo> query = currentSession().createNativeQuery(listNumberedOrderByLiveTimeDesc, HuYaLiveInfo.class);
        query.setParameter("number", number);
        return query.list();
    }
}
