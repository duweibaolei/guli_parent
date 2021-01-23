package com.dwl.service_edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 */
@SpringBootApplication
@EnableDiscoveryClient // 服务注册
@EnableFeignClients // 服务调用
@ComponentScan(basePackages = "com.dwl")
public class EduApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }

}
