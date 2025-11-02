package com.sangui.sanguimall.admin.config.security;


import com.sangui.sanguimall.admin.config.filter.TokenVerifyFilter;
import com.sangui.sanguimall.admin.config.handler.MyAuthenticationFailureHandler;
import com.sangui.sanguimall.admin.config.handler.MyAuthenticationSuccessHandler;
import com.sangui.sanguimall.admin.config.handler.MyLogoutSuccessHandler;
import com.sangui.sanguimall.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: SpringSecurity 配置文件
 * @Version: 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Resource
    private TokenVerifyFilter tokenVerifyFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource corsConfigurationSource) throws Exception {
        // 禁用跨站请求伪造
        return httpSecurity.formLogin((formLogin)->{
                    formLogin.loginProcessingUrl(Constants.LOGIN_URI)
                            .usernameParameter(Constants.NAME_OF_USERNAME_IN_USER)
                            .passwordParameter(Constants.NAME_OF_PASSWORD_IN_USER)
                            .successHandler(myAuthenticationSuccessHandler)
                            .failureHandler(myAuthenticationFailureHandler)
                            ;
                })
                .authorizeHttpRequests((authorize)->{
                    // 任何请求都需要登录后才能访问，除了 "/api/login"
                    authorize.requestMatchers(Constants.LOGIN_URI).permitAll()
                            .anyRequest().authenticated();
                })
                // 方法引用 禁用跨站请求伪造
                .csrf(AbstractHttpConfigurer::disable)
                // 支持跨域请求
//                .cors((cors) ->{
//                    cors.configurationSource(corsConfigurationSource);
//                })
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenVerifyFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> {
                    logout.logoutUrl("/sysUser/logout").logoutSuccessHandler(myLogoutSuccessHandler);
                })
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        // 允许任何来源，http://localhost:8080
        //corsConfiguration.setAllowedOrigins(List.of("*"));
        // 运行任何方式，post, get, delete, put
        corsConfiguration.setAllowedMethods(List.of("*"));
        // 设置运行的请求头
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(false);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
}
