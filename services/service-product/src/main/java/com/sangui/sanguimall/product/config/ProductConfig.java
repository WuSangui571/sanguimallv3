package com.sangui.sanguimall.product.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: 配置类
 * @Version: 1.0
 */
@Configuration
public class ProductConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
