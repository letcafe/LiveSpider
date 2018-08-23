package com.letcafe.service.impl;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.dao.HuYaUserLevelDao;
import com.letcafe.service.HuYaUserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HuYaUserLevelServiceImpl implements HuYaUserLevelService {

    private HuYaUserLevelDao userLevelDao;

    @Autowired
    public HuYaUserLevelServiceImpl(HuYaUserLevelDao userLevelDao) {
        this.userLevelDao = userLevelDao;
    }

    @Override
    public void save(HuYaUserLevel huYaUserLevel) {
        userLevelDao.save(huYaUserLevel);
    }
}
