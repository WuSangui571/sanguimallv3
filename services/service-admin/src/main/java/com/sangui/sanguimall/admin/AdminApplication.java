package com.sangui.sanguimall.admin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author sangui
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.sangui.sanguimall.admin.mapper"})
public class AdminApplication implements CommandLineRunner {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public void run(String... args) throws Exception {
        // SpringBoot 项目启动后，把 redisTemplate 这个 Bean 修改一下，修改一下 key 和 value 的序列化方式

        // 设置 key 序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 对象映射工具，Java 对象和 json 对象进行相互转化
        ObjectMapper mapper = new ObjectMapper();
        // 设置可见性
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 激活类型
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING);

        // 设置 value 序列化
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper, Object.class));

        // 设置 hashKey 序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 设置 hashValue 序列化
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper, Object.class));

    }

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
