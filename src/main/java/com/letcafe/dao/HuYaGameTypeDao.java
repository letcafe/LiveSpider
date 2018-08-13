package com.letcafe.dao;

import com.letcafe.bean.HuYaGameType;

public interface HuYaGameTypeDao {
    void save(HuYaGameType huYaGameType);

    void saveOrUpdate(HuYaGameType huYaGameType);
}
