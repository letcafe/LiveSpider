package com.letcafe.service.impl;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.dao.HuYaGameTypeDao;
import com.letcafe.service.HuYaGameTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HuYaGameTypeServiceImpl implements HuYaGameTypeService{

    private HuYaGameTypeDao huYaGameTypeDao;

    @Autowired
    public HuYaGameTypeServiceImpl(HuYaGameTypeDao huYaGameTypeDao) {
        this.huYaGameTypeDao = huYaGameTypeDao;
    }

    @Override
    public void save(HuYaGameType huYaGameType) {
        huYaGameTypeDao.save(huYaGameType);
    }

    @Override
    public void saveOrUpdateList(List<HuYaGameType> huYaGameTypes) {

        for (HuYaGameType huYaGameType : huYaGameTypes) {
            huYaGameTypeDao.saveOrUpdate(huYaGameType);
        }
    }
}
