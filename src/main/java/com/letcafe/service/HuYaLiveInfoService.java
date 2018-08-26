package com.letcafe.service;

import com.letcafe.bean.HuYaLiveInfo;

import java.util.List;

public interface HuYaLiveInfoService {
    void saveOrUpdate(HuYaLiveInfo huYaLiveInfo);

    List<HuYaLiveInfo> listNumberedOrderByLiveTimeDesc(int number);
}
