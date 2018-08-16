package com.letcafe.dao;

import com.letcafe.bean.mongo.LiveInfoLog;

public interface LiveInfoLogDao {
    void save(LiveInfoLog liveInfoLog);

    Long countAll();
}
