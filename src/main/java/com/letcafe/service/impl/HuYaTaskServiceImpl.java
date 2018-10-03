package com.letcafe.service.impl;

import com.letcafe.bean.HuYaTask;
import com.letcafe.dao.HuYaTaskDao;
import com.letcafe.service.HuYaTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HuYaTaskServiceImpl implements HuYaTaskService {

    private HuYaTaskDao huYaTaskDao;

    @Autowired
    public HuYaTaskServiceImpl(HuYaTaskDao huYaTaskDao) {
        this.huYaTaskDao = huYaTaskDao;
    }

    @Override
    public void save(HuYaTask huYaTask) {
        huYaTaskDao.save(huYaTask);
    }
}
