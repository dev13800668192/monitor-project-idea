package com.sefon.monitorproject.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Queue;

/**
 * @author by
 * @version 1.0
 * @date 2020/7/28 20:43
 */
public interface QueueService {

    /**
     * 设置队列
     * @param dataQueue
     * @param cacheQueue
     * @param json
     */
    void setQueue(Queue dataQueue, Queue cacheQueue, JSONObject json);
}
