package com.sefon.monitorproject.service;

import com.sefon.monitorproject.dao.ClientDataDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Queue;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 20:49
 */
public interface ClientDataService {

    int insertClientData(Queue queue);

    List<ClientDataDao> findAllData(String time1,String time2);

}
