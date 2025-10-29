package com.sangui.sanguimall.util;


import org.springframework.util.ObjectUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: 缓存工具类
 * @Version: 1.0
 */
public class CacheUtils {
    /**
     * 带有缓存的查询工具方法
     *
     * @param cacheSelector cacheSelector
     * @param databaseSelector databaseSelector
     * @param cacheSave cacheSave
     * @param <T> <T>
     * @return <T>
     */
    public static <T> T getCacheData(Supplier<T> cacheSelector, Supplier<T> databaseSelector, Consumer<T> cacheSave) {
        // 从 redis 查询
        T data = cacheSelector.get();
        // 如果 redis 没查到
        if (ObjectUtils.isEmpty(data)) {
            // 从数据库查
            data = databaseSelector.get();
            // 数据库查到了数据
            if (!ObjectUtils.isEmpty(data)) {
                // 把数据放入 redis
                cacheSave.accept(data);
            }
        }
        // 返回数据
        return data;
    }
}
