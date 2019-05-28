package com.letcafe.service.impl;

import com.letcafe.bean.mongo.LiveInfoLog;
import com.letcafe.dao.LiveInfoLogDao;
import com.letcafe.service.LiveInfoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LiveInfoLogServiceImpl implements LiveInfoLogService {

    private LiveInfoLogDao liveInfoLogDao;

    @Autowired
    public LiveInfoLogServiceImpl(LiveInfoLogDao liveInfoLogDao) {
        this.liveInfoLogDao = liveInfoLogDao;
    }

    @Override
    public void save(LiveInfoLog liveInfoLog) {
        liveInfoLogDao.save(liveInfoLog);
    }

    @Override
    public Long countAll() {
        return liveInfoLogDao.countAll();
    }
}
