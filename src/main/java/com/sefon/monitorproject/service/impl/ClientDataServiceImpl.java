package com.sefon.monitorproject.service.impl;

import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.mapper.ClientDataMapper;
import com.sefon.monitorproject.service.ClientDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 16:31
 */
@Service
public class ClientDataServiceImpl implements ClientDataService {

    @Autowired
    private ClientDataMapper clientDataMapper;



    @Override
    @Cacheable(value = "ClientData")
    public void insertClientData(Queue queue) {
        List<ClientDataDao> data =(List<ClientDataDao>) queue;
        clientDataMapper.insertList(data);
    }

    @Override
    public List<ClientDataDao> findAllData(String minTime,String maxTime) {
        if (minTime!=""&&maxTime!=""){
            return clientDataMapper.findDataByTime(minTime, maxTime);
        }else {
            return clientDataMapper.selectAll();
        }
    }

}
