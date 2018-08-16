package com.letcafe.dao;

import com.letcafe.bean.HuYaGameType;

import java.util.List;

public interface HuYaGameTypeDao {
    void save(HuYaGameType huYaGameType);

    void saveOrUpdate(HuYaGameType huYaGameType);

    List<HuYaGameType> listHuYaGameType();

    List<Integer> listAllGid();
}
