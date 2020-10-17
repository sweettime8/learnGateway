package com.elcom.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableCaching
//@EnableScheduling
//@EnableDiscoveryClient
public class ElcomServiceApplication {
    
    public static void main(String[] args) {
        // Fix lỗi "UDP failed setting ip_ttl | Method not implemented" khi start app trên Windows
        System.setProperty("java.net.preferIPv4Stack", "true");
        
        SpringApplication.run(ElcomServiceApplication.class, args);
    }
}
