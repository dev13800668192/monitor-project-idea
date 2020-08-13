package com.sefon.monitorproject;

import com.sefon.monitorproject.dao.ClientDataDao;
import com.sefon.monitorproject.service.ClientDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class MonitorProjectApplicationTests {
    @Autowired
    private ClientDataService clientDataService;
    private List<ClientDataDao> allData = new ArrayList<>();
    private List<ClientDataDao> list = new ArrayList<>();

    @Test
    void contextLoads() {
        ClientDataDao data = new ClientDataDao();
        data.setCpu("0");
        data.setFps("0");
        data.setGpu("0");
        data.setHardDisk("0");
        data.setIo("0");
        data.setMemory("0");

        allData = clientDataService.findAllData("", "","");
        Date max = allData.get(allData.size() - 1).getUpdateTime();
        Date min = allData.get(0).getUpdateTime();
        Date other = allData.get(1).getUpdateTime();
        long othertime = other.getTime() - min.getTime();
        long time = max.getTime() - min.getTime();
        System.out.println();
        int difference = (int) (time / othertime);
        for(int i=0;i<=difference;i++ ){
            if(allData.get(i).getUpdateTime().getTime()!=min.getTime()+othertime*i){
                data.setIp(allData.get(i).getIp());
                data.setHostname(allData.get(i).getHostname());
                data.setUpdateTime(new Date(min.getTime()+time*i));
                allData.add(i,data);
            }
        }
        System.out.println(allData.size());
    }
}

