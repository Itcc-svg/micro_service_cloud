package com.itcc.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class SpringCloudDashboardApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudDashboardApp.class, args);
    }
}
