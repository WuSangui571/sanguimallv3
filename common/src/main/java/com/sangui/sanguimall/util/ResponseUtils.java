package com.sangui.sanguimall.util;


import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: Response 工具类
 * @Version: 1.0
 */
public class ResponseUtils {

    /**
     * 使用 response，把结果写出到前端
     *
     * @param response 响应
     * @param result 结果
     */
    public static void write(HttpServletResponse response, String result) {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(result);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
