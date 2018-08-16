package com.letcafe.service.impl;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.dao.HuYaLiveInfoDao;
import com.letcafe.service.HuYaLiveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HuYaLiveInfoServiceImpl implements HuYaLiveInfoService {

    private HuYaLiveInfoDao huYaLiveInfoDao;

    @Autowired
    public HuYaLiveInfoServiceImpl(HuYaLiveInfoDao huYaLiveInfoDao) {
        this.huYaLiveInfoDao = huYaLiveInfoDao;
    }

    @Override
    public void saveOrUpdate(HuYaLiveInfo huYaLiveInfo) {
        huYaLiveInfoDao.saveOrUpdate(huYaLiveInfo);
    }
}
