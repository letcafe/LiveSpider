package com.letcafe.dao;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.dao.jdbc.HuYaLiveInfoJdbc;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HuYaLiveInfoDao extends JpaRepository<HuYaLiveInfo, Long>, HuYaLiveInfoJdbc {
    @Override
    void saveOrUpdate(HuYaLiveInfo huYaLiveInfo);

    @Query(value = "SELECT * FROM huya_live_info ORDER BY update_time DESC LIMIT :number", nativeQuery = true)
    List<HuYaLiveInfo> listNumberedOrderByLiveTimeDesc(@Param("number") Integer number);
}
