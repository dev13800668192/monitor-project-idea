package com.sefon.monitorproject.service.impl;

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
    public void insertClientData(List<ClientDataDao> clientList) {
        clientDataMapper.insertList(clientList);
    }

    @Override
    public void insertDevices(List<DeviceDao> devices) {
        deviceMapper.insertList(devices);
    }

    /**
     * 根据ip查询对应ip的所有客户端性能数据
     * @param minTime
     * @param maxTime
     * @param ip
     * @return
     */
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
     * @param allData
     * @return
     */
    @Override
    public List<ClientDataDao> returnAllData(List<ClientDataDao> allData,String ip) {

        List<ClientDataDao> dataList = new ArrayList<>();
//        List<ClientDataDao> retrunList =new ArrayList<>();

        if(allData.size()==0) {
            allData = findAllData("", "", ip);
        }
        for (ClientDataDao o: allData) {
            if (o.getIp().equals(ip)) {
                dataList.add(o);
            }
        }
        if(dataList.size()==0){
            return dataList;
        }

        Date star = dataList.get(0).getUpdateTime();
        Date end = dataList.get(dataList.size() - 1).getUpdateTime();

//        for (long time=star.getTime();time<=end.getTime();time+=5000){
//            ClientDataDao obj = new ClientDataDao();
//            obj.setCpu("0");
//            obj.setFps("0");
//            obj.setGpu("0");
//            obj.setHardDisk("0");
//            obj.setIo("0");
//            obj.setMemory("0");
//            obj.setIp("");
//            obj.setHostname("");
//            obj.setUpdateTime(new Date(time));
//            retrunList.add(obj);
//        }
//        for (ClientDataDao data: dataList) {
//            for (int index=0;index<retrunList.size();index++){
//                if (retrunList.get(index).getUpdateTime().toString().equals(data.getUpdateTime().toString())){
//                    retrunList.set(index,data);
//                }
//            }
//        }
//        dataList.clear();
//        return retrunList;

        long difference =end.getTime()-star.getTime();
        long interval =5000;
        int num =(int) (difference / interval);

        for(int i=0;i<=num;i++ ){
            if(dataList.get(i).getUpdateTime().getTime()!=(star.getTime()+interval*i)){
                ClientDataDao obj = new ClientDataDao();
                obj.setCpu("0");
                obj.setFps("0");
                obj.setGpu("0");
                obj.setHardDisk("0");
                obj.setIo("0");
                obj.setMemory("0");
                obj.setIp(dataList.get(i).getIp());
                obj.setHostname(dataList.get(i).getHostname());
                obj.setUpdateTime(new Date(star.getTime()+interval*i));
                if(dataList.get(i).getUpdateTime().getTime()%interval==0){
                    dataList.add(i,obj);
                }else {
                    dataList.set(i, obj);
                }
            }
        }
        return dataList;
    }

    /**
     * 查询数据库中的设备信息
     * @return
     */
    @Override
    public List<DeviceDao> findDevice() {
        return deviceMapper.findDevices();
    }

    /**
     * 返回客户端各项性能数据列表
     * @param list
     * @param cpuList
     * @param gpuList
     * @param memoryList
     * @param fpsList
     * @param hardDiskList
     * @param ioList
     * @param updateTimeList
     * @return
     */
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
