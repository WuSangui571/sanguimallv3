//package com.sangui.sanguimall.gateway.filter;
//
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * @Author: sangui
// * @CreateTime: 2025-11-02
// * @Description:
// * @Version: 1.0
// */
//@Component
//public class ConditionalAddHeaderFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 获取前端请求头中的 Authorization
//        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//        // 如果 token 不为空，则添加到下游请求
//        if (token != null && !token.isEmpty()) {
//            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
//                    .header("Authorization", token)
//                    .build();
//
//            // 构建新的 exchange 并继续调用 filter chain
//            return chain.filter(exchange.mutate().request(mutatedRequest).build());
//        }
//
//        // token 为空，直接继续调用 filter chain
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        // 顺序可以根据需求调整
//        // -1 表示在默认 Filter 之前执行
//        return -1;
//    }
//}
