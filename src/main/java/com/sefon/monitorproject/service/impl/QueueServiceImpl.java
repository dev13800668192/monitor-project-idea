package com.sefon.monitorproject.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.dao.DeviceDao;
import com.sefon.monitorproject.service.QueueService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/29 9:11
 */
@Service
public class QueueServiceImpl implements QueueService {
    @Override
    public void setQueue(Queue dataQueue, Queue cacheQueue, JSONObject json,Map<String,List<ClientDataDao>> cacheDataList) {
        ClientDataDao clientDataDao = JSON.parseObject(JSON.toJSONString(json.get("data")), ClientDataDao.class);

        boolean flag = true;

        for (String key: cacheDataList.keySet()
             ) {
            if (clientDataDao.getIp().equals(key)){
                flag=false;
                cacheDataList.get(key).add(clientDataDao);
            }

            if (cacheDataList.get(key).size()>=2){
                cacheDataList.get(key).remove(0);
            }
        }

        if (flag){
            List<ClientDataDao> newCacheData = new ArrayList<>();
            newCacheData.add(clientDataDao);
            cacheDataList.put(clientDataDao.getIp(), newCacheData);
        }

        dataQueue.offer(clientDataDao);
//        cacheQueue.offer(clientDataDao);
//        if (cacheQueue.size() >= 2) {
//            cacheQueue.poll();
//        }
    }

    @Override
    public List<DeviceDao> setDevice(Queue dataQueue, List<DeviceDao> devices) {
        List<DeviceDao> list = new ArrayList<>();
        List<ClientDataDao> datas = (List<ClientDataDao>) dataQueue;
        for (ClientDataDao data : datas
        ) {
            boolean flag = true;
            for (DeviceDao device : devices
            ) {
                if (data.getIp().equals(device.getIp())) {
                    flag = false;
                    device.setState("1");
                }
            }
            if (flag) {
                DeviceDao obj = new DeviceDao(data.getIp(), data.getHostname(), "0");
                list.add(obj);
                devices.add(obj);
            }
        }
        return list;
    }
}
