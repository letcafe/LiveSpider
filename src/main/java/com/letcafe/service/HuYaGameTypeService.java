package com.letcafe.service;

import com.letcafe.bean.HuYaGameType;

import java.util.List;

public interface HuYaGameTypeService {

    void save(HuYaGameType huYaGameType);

    void saveOrUpdateList(List<HuYaGameType> huYaGameTypes);

    List<HuYaGameType> listHuYaGameType();

    List<Integer> listAllGid();
}
