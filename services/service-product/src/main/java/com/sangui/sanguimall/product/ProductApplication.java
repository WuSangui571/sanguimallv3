package com.sangui.sanguimall.product;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: 商品微服务的主入口程序
 * @Version: 1.0
 */
@SpringBootApplication
@MapperScan("com.sangui.sanguimall.product.mapper")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
