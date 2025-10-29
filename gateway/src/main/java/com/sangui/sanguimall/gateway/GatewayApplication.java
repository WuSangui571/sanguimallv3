package com.sangui.sanguimall.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: gateway 的主入口程序
 * @Version: 1.0
 */
// 加上注解开启发现
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
