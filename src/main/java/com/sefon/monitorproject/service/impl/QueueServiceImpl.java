package com.sefon.monitorproject.service.impl;

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
    /**
     * 处理数据，放入对应ip的数据列表中
     * @param clientList 数据存储列表，入库使用
     * @param obj 接收的数据对象
     * @param clientDataMap 缓存数据集，根据ip分组
     */
    @Override
    public void setQueue(List<ClientDataDao> clientList,
                         ClientDataDao obj,
                         Map<String,List<ClientDataDao>> clientDataMap,
                         Map<String,String> timeMap) {

        /*
         * 根据客户端ip,将数据放入对应的List中
         * 若缓存列表中存在对应的ip列表，更新数据，否则创建对应ip列表
         */
        if(clientDataMap.get(obj.getIp())!=null){
            ClientDataDao remove = clientDataMap.get(obj.getIp()).remove(0);
            timeMap.put(obj.getIp(),remove.getUpdateTime().toString());
            clientDataMap.get(obj.getIp()).add(obj);
        }else{
            List<ClientDataDao> newCacheData = new ArrayList<>();
            newCacheData.add(obj);
            clientDataMap.put(obj.getIp(), newCacheData);
            timeMap.put(obj.getIp(),"");
        }

        /*
        将数据放入存储列表，存入数据库
         */
        clientList.add(obj);
    }

    @Override
    public List<DeviceDao> setDevice(Map<String,String> timeMap, List<DeviceDao> devices) {

        List<DeviceDao> list = new ArrayList<>();

        for (String time: timeMap.keySet()) {
            boolean flag = true;

            /*
            判断是否存在新客户端数据
             */
            for (DeviceDao device : devices) {
                if (time.equals(device.getIp())){
                    flag = false;
                }
            }

            /*
            将新客户端设备信息存入数据库
             */
            if (flag) {
                DeviceDao obj = new DeviceDao(time, "", "0");
                list.add(obj);
                devices.add(obj);
            }
        }

        return list;
    }

    /**
     * 设置设备在线状态
     * @param timeMap
     * @param clientCacheMap
     * @return
     */
    @Override
    public List<DeviceDao> getDevice(Map<String, String> timeMap,
                                     Map<String,List<ClientDataDao>> clientCacheMap,
                                     List<DeviceDao> devices) {

        for (DeviceDao device :devices){
            if (!clientCacheMap.get(device.getIp()).get(0).getUpdateTime().toString()
                    .equals(timeMap.get(device.getIp()))){
                    device.setState("1");
            }
        }

        return devices;
    }
}
