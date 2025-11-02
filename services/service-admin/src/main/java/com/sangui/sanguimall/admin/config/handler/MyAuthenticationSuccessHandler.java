package com.sangui.sanguimall.admin.config.handler;


import com.sangui.sanguimall.admin.model.entity.SysUser;
import com.sangui.sanguimall.admin.service.RedisService;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.result.R;
import com.sangui.sanguimall.util.JsonUtils;
import com.sangui.sanguimall.util.JwtUtils;
import com.sangui.sanguimall.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: SysUser 登录成功的处理器
 * @Version: 1.0
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SysUser sysUser = (SysUser) authentication.getPrincipal();

        // 把 tUser 对象转成 json 作为负载数据放入 jwt
        String userJson = JsonUtils.toJson(sysUser);
        String jwt = JwtUtils.createJwt(userJson);
        System.out.println("jwt:" + jwt);

        // 在 Redis 中放入此 jwt
        redisService.setValue(Constants.REDIS_JWT_KEY + sysUser.getUserId(),jwt);

        // 设置 jwt 的过期时间（如果选择了记住我，过期时间是 7 天，否则是 30 min）
        String rememberMe = request.getParameter("rememberMe");
        if (Boolean.parseBoolean(rememberMe)){
            redisService.expire(Constants.REDIS_JWT_KEY + sysUser.getUserId(),Constants.EXPIRE_TIME, TimeUnit.SECONDS);
        }else {
            redisService.expire(Constants.REDIS_JWT_KEY + sysUser.getUserId(),Constants.DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
        }

        // 登录成功的统一结果
        R result = R.ok(jwt);

        // 把 R 对象转成 json
        String resultJson = JsonUtils.toJson(result);

        // 把 R 以 json 返回给前端
        ResponseUtils.write(response, resultJson);
    }
}
