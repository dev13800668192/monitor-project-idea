package com.sefon.monitorproject.controller;

import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.dao.DeviceDao;
import com.sefon.monitorproject.service.ClientDataService;
import com.sefon.monitorproject.service.QueueService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 9:44
 */
@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Autowired
    private ClientDataService clientDataService;

    @Autowired
    private QueueService queueService;

    private Map<String,List<ClientDataDao>> clientCacheMap = new HashMap<>();
    private Map<String,String> timeMap = new HashMap<>();
    private List<ClientDataDao> clientList = new ArrayList<>();
    private List<ClientDataDao> allData = new ArrayList<>();
    private List<DeviceDao> devices = new ArrayList<>();





    /**
     * 提供接口，用于客户端返回性能数据
     * @param obj
     * @return
     */
    @PostMapping("/client/pushData")
    public String pushData(@RequestBody ClientDataDao obj){
        /*
        设置数据缓存列表
         */
        queueService.setQueue(clientList,obj,clientCacheMap,timeMap);

        /*
        判断是否有新设备接入
         */
        List<DeviceDao> newDevices = queueService.setDevice(timeMap, devices);

        /*
        新设备入库
         */
        if (newDevices.size()>0){
            clientDataService.insertDevices(newDevices);
        }

        /*
        当缓存数据大于60条时入库，并清空缓存列表
         */
        if (clientList.size()>=60) {
            clientDataService.insertClientData(clientList);
            clientList.clear();
        }

        /*
        客户端返回信息
         */
        return "true";
    }

    /**
     * 获得实时数据缓存队列
     * @return
     */
    @GetMapping("/client/cacheData")
    public List<ClientDataDao> getClientCacheDate(@Param("ip") String ip){
        return clientCacheMap.get(ip);
    }

    /**
     * 返回设备列表
     * @return
     */
    @GetMapping("/devices/getDevice")
    public List<DeviceDao> getDevices(){
        return queueService.getDevice(timeMap,clientCacheMap,clientDataService.findDevice());
    }
    /**
     * 根据时间条件展示历史数据
     * @param minTime
     * @param maxTime
     * @return
     */
    @PostMapping("/client/AllData")
    public List<Map<String,Object>> getClientAllDate
            (@RequestParam(value = "minTime",defaultValue = "") String minTime,
             @RequestParam(value = "maxTime",defaultValue = "") String maxTime,
             @RequestParam(name = "ip",defaultValue = "")String ip){
        List<String> cpuList=new ArrayList<>();
        List<String> gpuList=new ArrayList<>();
        List<String> memoryList=new ArrayList<>();
        List<String> fpsList=new ArrayList<>();
        List<String> hardDiskList=new ArrayList<>();
        List<String> ioList=new ArrayList<>();
        List<String> updateTimeList=new ArrayList<>();



        if (!"".equals(minTime)&&!"".equals(maxTime)){
            return clientDataService.paramDataList(clientDataService.returnAllData(clientDataService.findAllData(minTime, maxTime,ip),ip),
                    cpuList,gpuList,memoryList,fpsList,hardDiskList,ioList,updateTimeList);
        }else{
            return clientDataService.paramDataList(clientDataService.returnAllData(allData,ip),
                    cpuList,gpuList,memoryList,fpsList,hardDiskList,ioList,updateTimeList);
        }
    }

    /**
     * 定时更新缓存的全部数据
     */
    @PostConstruct
    @Scheduled(cron = "0/2 * * * * ?")
    public void updateAllData() {
        allData.clear();
        allData=clientDataService.findAllData("","","");
    }

    @PostConstruct
    public void updateDevice() {
        devices=clientDataService.findDevice();
    }
}
