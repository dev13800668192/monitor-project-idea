package com.sefon.monitorproject.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.dao.DeviceDao;
import com.sefon.monitorproject.mapper.ClientDataMapper;
import com.sefon.monitorproject.mapper.DeviceMapper;
import com.sefon.monitorproject.service.ClientDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 16:31
 */
@Service
public class ClientDataServiceImpl implements ClientDataService {

    @Autowired
    private ClientDataMapper clientDataMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    @Cacheable(value = "ClientData")
    public void insertClientData(Queue queue) {
        List<ClientDataDao> data =(List<ClientDataDao>) queue;
        clientDataMapper.insertList(data);
    }

    @Override
    public void insertDevices(List<DeviceDao> devices) {
        deviceMapper.insertList(devices);
    }

    @Override
    public List<ClientDataDao> findAllData(String minTime,String maxTime,String ip) {
        if (!"".equals(minTime)&&!"".equals(maxTime)){
            return clientDataMapper.findDataByTime(minTime, maxTime,ip);
        }else{
            return clientDataMapper.findAllData();
        }
    }

    /**
     * 处理返回的全部数据
     * @param list
     * @return
     */
    @Override
    public List<ClientDataDao> returnAllData(List<ClientDataDao> list,String ip) {
        List<ClientDataDao> retrunList = new ArrayList<>();
        if(list.size()==0){
           list = findAllData("","",ip);
            for (ClientDataDao o: list) {
                if (o.getIp().equals(ip)) {
                    retrunList.add(o);
                }
            }
        }else{
            for (ClientDataDao o: list) {
                if (o.getIp().equals(ip)) {
                    retrunList.add(o);
                }
            }
            if (retrunList.size()==0){
                return retrunList;
            }
        }

        Date max = retrunList.get(retrunList.size() - 1).getUpdateTime();
        Date min = retrunList.get(0).getUpdateTime();
//            计算时间差
        long difference =max.getTime()-min.getTime();
//            计算时间间隔
        long interval = retrunList.get(1).getUpdateTime().getTime()-retrunList.get(0).getUpdateTime().getTime();

        int num =(int) (difference / interval);

        for(int i=0;i<=num;i++ ){
            if(retrunList.get(i).getUpdateTime().getTime()!=(min.getTime()+interval*i)){
                Date d =retrunList.get(i).getUpdateTime();
                Date d2 = new Date(min.getTime()+interval*i);
                ClientDataDao obj = new ClientDataDao();
                obj.setCpu("0");
                obj.setFps("0");
                obj.setGpu("0");
                obj.setHardDisk("0");
                obj.setIo("0");
                obj.setMemory("0");
                obj.setIp(retrunList.get(i).getIp());
                obj.setHostname(retrunList.get(i).getHostname());
                obj.setUpdateTime(new Date(min.getTime()+interval*i));
                if(retrunList.get(i).getUpdateTime().getTime()%interval==0){
                    retrunList.add(i,obj);
                }else {
                    retrunList.set(i, obj);
                }
            }
        }

        return retrunList;
    }

    @Override
    public List<DeviceDao> findDevice() {
        return deviceMapper.findDevices();
    }

    @Override
    public List<Map<String,Object>> paramDataList(List<ClientDataDao> list,
                                   List<String> cpuList, List<String> gpuList,
                                   List<String> memoryList, List<String> fpsList,
                                   List<String> hardDiskList, List<String> ioList,
                                   List<String> updateTimeList) {
        List<Map<String,Object>> datas = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        for (ClientDataDao o:list
             ) {
            cpuList.add(o.getCpu());
            gpuList.add(o.getGpu());
            memoryList.add(o.getMemory());
            fpsList.add(o.getFps());
            hardDiskList.add(o.getHardDisk());
            ioList.add(o.getIo());
            updateTimeList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o.getUpdateTime()));
        }
        map.put("cpu",cpuList);
        map.put("gpu",gpuList);
        map.put("memory",memoryList);
        map.put("fps",fpsList);
        map.put("hardDisk",hardDiskList);
        map.put("io",ioList);
        map.put("updateTime",updateTimeList);

        datas.add(map);

        return datas;
    }

}
