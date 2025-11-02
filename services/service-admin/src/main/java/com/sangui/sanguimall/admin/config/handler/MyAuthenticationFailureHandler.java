package com.sangui.sanguimall.admin.config.handler;


import com.sangui.sanguimall.result.R;
import com.sangui.sanguimall.util.JsonUtils;
import com.sangui.sanguimall.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: sysUser 登录失败的处理器
 * @Version: 1.0
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 登录失败，执行该方法，在该方法中返回 json 给前端，就行了
        // 登录失败的统一结果
        R result = R.fail(exception.getMessage());

        // 把 R 对象转成 json
        String resultJson = JsonUtils.toJson(result);

        // 把 R 以json返回给前端
        ResponseUtils.write(response, resultJson);
    }
}
