package com.sangui.sanguimall.config;


import com.sangui.sanguimall.result.CodeEnum;
import com.sangui.sanguimall.result.R;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: 统一异常处理类，Controller 发生了异常，统一用该类进行处理
 * @Version: 1.0
 */
// aop。拦截标注了 @RestController 的controller 中的所有方法
@RestControllerAdvice
// aop。拦截标注了 @Controller 的 controller 中的所有方法
// @ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 异常处理的方法（Controller 方法发生了异常，那么就使用该方法来处理）
     * @return 响应结果
     */
    @ExceptionHandler(value = Exception.class)
    public R handException(Exception e) {
        // 在控制台打印异常信息
        e.printStackTrace();
        return R.fail(e.getMessage());
    }

    /**
     * 异常的精确匹配，先精确匹配，匹配不到了，就找父类的异常处理
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(value = DataAccessException.class)
    public R handException3(DataAccessException e) {
        // 在控制台打印异常信息
        e.printStackTrace();
        return R.fail(CodeEnum.DATA_ACCESS_EXCEPTION);
    }

    /**
     * 权限不足的异常处理
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public R handException(AccessDeniedException e) {
        // 在控制台打印异常信息
        e.printStackTrace();
        return R.fail(CodeEnum.ACCESS_DENIED);
    }
}
