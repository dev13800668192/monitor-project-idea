package com.sefon.monitorproject.dao;

import javax.persistence.Table;

@Table(name = "设备列表")
public class DeviceDao {

    private String ip;

    private String hostname;

    private String state;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public DeviceDao(String ip, String hostname, String state) {
        this.ip = ip;
        this.hostname = hostname;
        this.state = state;
    }

    public DeviceDao() {
    }
}
