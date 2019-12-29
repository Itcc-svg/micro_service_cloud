package com.itcc.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 不能加入config-server依赖，否则rest controller中的@value注解会报错
 */
@SpringBootApplication
public class ConfigClient_3355_StartSpringCloudApp {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClient_3355_StartSpringCloudApp.class, args);
    }
}