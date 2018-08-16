package com.letcafe.service;

import com.letcafe.bean.mongo.LiveInfoLog;

public interface LiveInfoLogService {

    void save(LiveInfoLog liveInfoLog);

    Long countAll();
}
