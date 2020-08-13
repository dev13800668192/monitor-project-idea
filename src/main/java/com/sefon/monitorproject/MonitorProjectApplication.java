package com.sefon.monitorproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.sefon.monitorproject.mapper")
public class MonitorProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorProjectApplication.class, args);
    }

}
