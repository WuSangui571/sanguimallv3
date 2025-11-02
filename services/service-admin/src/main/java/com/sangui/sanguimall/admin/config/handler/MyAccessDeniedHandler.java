package com.sangui.sanguimall.admin.config.handler;


import com.sangui.sanguimall.result.CodeEnum;
import com.sangui.sanguimall.result.R;
import com.sangui.sanguimall.util.JsonUtils;
import com.sangui.sanguimall.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: 没有权限时的处理器，没有权限访问，执行下面的方法，在该方法中返回 json 给前端，就行了
 * @Version: 1.0
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 登录失败的统一结果
        R result = R.fail(CodeEnum.ACCESS_DENIED);

        // 把R对象转成 json
        String resultJson = JsonUtils.toJson(result);

        // 把 R 以 json 返回给前端
        ResponseUtils.write(response, resultJson);
    }
}
