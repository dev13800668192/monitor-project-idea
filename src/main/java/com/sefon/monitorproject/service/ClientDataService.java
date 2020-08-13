package com.sefon.monitorproject.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.dao.DeviceDao;

import java.util.Date;
import java.util.List;
import java.util.Map;
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

    void insertDevices(List<DeviceDao> devices);

    /**
     * 返回数据库中所有客户端性能数据
     * @param time1
     * @param time2
     * @return List<ClientDataDao>
     */
    List<ClientDataDao> findAllData(String time1,String time2,String ip);

    List<ClientDataDao> returnAllData(List<ClientDataDao> list);

    List<DeviceDao> findDevice();


    List<Map<String,Object>> paramDataList(List<ClientDataDao> list,
                                           List<String> cpuList, List<String> gpuList,
                                           List<String> memoryList, List<String> fpsList,
                                           List<String> hardDiskList, List<String> ioList,
                                           List<String> updateTimeList);

}
