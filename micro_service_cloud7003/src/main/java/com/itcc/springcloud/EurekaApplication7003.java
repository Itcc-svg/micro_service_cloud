package com.itcc.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注解表明 springboot EnableEurekaServer
 * Eureka 通过localhost:7001检测
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication7003 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication7003.class, args);
    }
}
