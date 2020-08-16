package com.sefon.monitorproject.service;

import com.alibaba.fastjson.JSONObject;
import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.dao.DeviceDao;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 20:43
 */
public interface QueueService {

    void setQueue(List<ClientDataDao> clientList, ClientDataDao obj,
                  Map<String,List<ClientDataDao>> cacheDataList, Map<String,String> timeMap);

    List<DeviceDao> setDevice(Map<String,String> timeMap, List<DeviceDao> devices);

    List<DeviceDao> getDevice(Map<String,String> timeMap, Map<String,
            List<ClientDataDao>> clientCacheMap,List<DeviceDao> devices);
}
