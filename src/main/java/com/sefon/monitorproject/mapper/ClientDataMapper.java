package com.sefon.monitorproject.mapper;

import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.dao.DeviceDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 16:25
 */
public interface ClientDataMapper extends Mapper<ClientDataDao>, MySqlMapper<ClientDataDao> {

    @Select("SELECT id,ip,cpu,gpu,memory,fps,hardDisk,io,DATE_FORMAT(updateTime,'%Y-%m-%d %T') AS updateTime,hostname,pid FROM `客户端性能数据` WHERE updateTime< #{maxTime} AND updateTime> #{minTime} AND ip= #{ip}")
    List<ClientDataDao>  findDataByTime(@Param("minTime") String minTime, @Param("maxTime") String maxTime, @Param("ip") String ip);

    @Select("SELECT id,ip,cpu,gpu,memory,fps,hardDisk,io,DATE_FORMAT(updateTime,'%Y-%m-%d %T') AS updateTime,hostname,pid FROM `客户端性能数据`")
    List<ClientDataDao> findAllData();

}
