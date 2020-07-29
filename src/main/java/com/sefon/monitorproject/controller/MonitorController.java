package com.sefon.monitorproject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.service.ClientDataService;
import com.sefon.monitorproject.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private Queue clientQueue = new LinkedList();
    private Queue clientCacheQueue =new LinkedList();



    @PostMapping("/client/pushData")
    public String pushData(@RequestBody JSONObject json){

        queueService.setQueue(clientQueue,clientCacheQueue,json);

        if (clientQueue.size()>=2) {
            clientDataService.insertClientData(clientQueue);
            clientQueue.clear();
        }

        System.out.println(clientQueue);
        System.out.println(clientCacheQueue);

        return "true";
    }

    @GetMapping("/client/cacheData")
    public List<ClientDataDao> getClientCacheDate(){
        List<ClientDataDao> clientDataDaoList = (List<ClientDataDao>) clientCacheQueue;
        return clientDataDaoList;
    }

    @PostMapping("/client/AllData")
    public List<ClientDataDao> getClientAllDate
            (@RequestParam(value = "minTime",defaultValue = "") String minTime,
             @RequestParam(value = "maxTime",defaultValue = "") String maxTime){
        System.out.println(clientDataService.findAllData(minTime,maxTime));
        return clientDataService.findAllData(minTime,maxTime);
    }

}
