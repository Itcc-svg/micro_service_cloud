package com.itcc.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@MapperScan(value = "com.itcc.springcloud.mapper")
@SpringBootApplication
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //服务发现
public class DeptClientProvider8001 {

    /**
     * 通过修改github中的配置文件，就可以实现连接不同的数据库，修改配置等需求
     * 正规来说，应该是通过修改github上的文件来修改配置，而不用修改代码
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DeptClientProvider8001.class, args);
    }
}
