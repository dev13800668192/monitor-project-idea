package com.sefon.monitorproject.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.service.QueueService;
import org.springframework.stereotype.Service;

import java.util.Queue;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/29 9:11
 */
@Service
public class QueueServiceImpl implements QueueService {
    @Override
    public void setQueue(Queue dataQueue, Queue cacheQueue, JSONObject json) {
        ClientDataDao clientDataDao= JSON.parseObject(JSON.toJSONString(json.get("data")),ClientDataDao.class);

        dataQueue.offer(clientDataDao);
        cacheQueue.offer(clientDataDao);
        if (cacheQueue.size()>=2){
            cacheQueue.poll();
        }
    }
}
