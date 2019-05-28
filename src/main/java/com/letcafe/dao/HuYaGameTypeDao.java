package com.letcafe.dao;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.dao.jdbc.HuYaGameTypeJdbc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface HuYaGameTypeDao extends JpaRepository<HuYaGameType, String>, HuYaGameTypeJdbc {

    @Override
    void saveOrUpdate(HuYaGameType huYaGameType);

    @Query(value = "SELECT gid FROM huya_game_type", nativeQuery = true)
    List<Integer> listAllGid();
}
