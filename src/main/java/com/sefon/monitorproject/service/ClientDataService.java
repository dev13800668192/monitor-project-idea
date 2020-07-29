package com.sefon.monitorproject.service;

import com.sefon.monitorproject.dao.ClientDataDao;

import java.util.List;
import java.util.Queue;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 20:49
 */
public interface ClientDataService {

    /**
     * 将队列中的客户端数据插入数据库
     * @param queue
     */
    void insertClientData(Queue queue);

    /**
     * 返回数据库中所有客户端性能数据
     * @param time1
     * @param time2
     * @return List<ClientDataDao>
     */
    List<ClientDataDao> findAllData(String time1,String time2);

}
