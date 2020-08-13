package com.sefon.monitorproject.mapper;

import com.sefon.monitorproject.dao.DeviceDao;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface DeviceMapper extends Mapper<DeviceDao>, MySqlMapper<DeviceDao> {

    @Select("SELECT * FROM `设备列表`")
    List<DeviceDao> findDevices();
}
