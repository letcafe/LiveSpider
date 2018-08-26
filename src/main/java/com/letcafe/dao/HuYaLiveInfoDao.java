package com.letcafe.dao;

import com.letcafe.bean.HuYaLiveInfo;

import java.util.List;

public interface HuYaLiveInfoDao {
    void saveOrUpdate(HuYaLiveInfo huYaLiveInfo);

    List<HuYaLiveInfo> listNumberedOrderByLiveTimeDesc(int number);
}
