package com.sangui.sanguimall.admin.service;


import java.util.concurrent.TimeUnit;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-02
 * @Description: RedisService
 * @Version: 1.0
 */
public interface RedisService {
    /**
     * 在 Redis 中存数据
     * @param key Redis 中的 key
     * @param value 存入的值
     */
    void setValue(String key, Object value);

    /**
     * 在 Redis 中取指定 key 的数据
     * @param key Redis 中的 key
     * @return 取到的数据
     */
    Object getValue(String key);

    /**
     * 删除在 Redis 中指定 key 的数据
     * @param key Redis 中的 key
     */
    void removeValue(String key);

    /**
     * 设置 Redis 中指定 key 的自动过期时间
     * @param key Redis 中的 key
     * @param timeOut 设置过期时间的数值，一般默认是秒为单位
     * @param timeUnit 设置 timeOut 的单位，一般是秒，即：TimeUnit.SECONDS
     */
    void expire(String key, Long timeOut, TimeUnit timeUnit);
}
