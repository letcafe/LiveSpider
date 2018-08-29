package com.letcafe.dao.impl;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.mongo.LiveInfoLog;
import com.letcafe.dao.LiveInfoLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class LiveInfoLogDaoImpl implements LiveInfoLogDao {

    @Autowired
    private MongoOperations mongo;

    @Override
    public void save(LiveInfoLog liveInfoLog) {
        mongo.save(liveInfoLog);
    }

    @Override
    public Long countAll() {
        Criteria where = new Criteria();
        Query query = Query.query(where);
        return mongo.count(query, LiveInfoLog.class);
    }
}
