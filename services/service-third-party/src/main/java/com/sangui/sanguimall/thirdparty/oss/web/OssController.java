package com.sangui.sanguimall.thirdparty.oss.web;


import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.sts20150401.models.AssumeRoleResponseBody;
import com.aliyun.tea.TeaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangui.sanguimall.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-08
 * @Description: 对象存储 OSS 的 Controller
 * @Version: 1.0
 */
@RestController
@RequestMapping("/oss")
public class OssController {

    // OSS 基础信息
    String bucket = "sanguimall-test";
    String region = "cn-beijing";
    String host = "sanguimall-test.oss-cn-beijing.aliyuncs.com";

    // 限定上传到 OSS 的文件前缀。
    String upload_dir = "test06";

    //指定过期时间，单位为秒。
    Long expire_time = 3600L;

    /**
     * 通过指定有效的时长（秒）生成过期时间。
     *
     * @param seconds 有效时长（秒）。
     * @return ISO8601 时间字符串，如："2014-12-01T12:00:00.000Z"。
     */
    public static String generateExpiration(long seconds) {
        // 获取当前时间戳（以秒为单位）
        long now = Instant.now().getEpochSecond();
        // 计算过期时间的时间戳
        long expirationTime = now + seconds;
        // 将时间戳转换为Instant对象，并格式化为ISO8601格式
        Instant instant = Instant.ofEpochSecond(expirationTime);
        // 定义时区为UTC
        ZoneId zone = ZoneOffset.UTC;
        // 将 Instant 转换为 ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(zone);
        // 定义日期时间格式，例如2023-12-03T13:00:00.000Z
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // 格式化日期时间
        String formattedDate = zonedDateTime.format(formatter);
        // 输出结果
        return formattedDate;
    }

    @GetMapping("/getPolicy")
    public R getPostSignatureForOssUpload() throws Exception {
        String dir = "test06/";
        String bucketName = "sanguimall-test";
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");

        if (accessKeyId == null || accessKeySecret == null) {
            return R.fail("OSS 密钥未配置");
        }

        Map<String, Object> policy = new HashMap<>();
        policy.put("expiration", generateExpiration(3600));

        List<Object> conditions = new ArrayList<>();
        conditions.add(Map.of("bucket", bucketName));  // 必须加！
        conditions.add(Arrays.asList("content-length-range", 1, 104857600));
        conditions.add(Arrays.asList("eq", "$success_action_status", "200"));
        conditions.add(Arrays.asList("starts-with", "$key", dir));

        policy.put("conditions", conditions);

        ObjectMapper mapper = new ObjectMapper();
        String jsonPolicy = mapper.writeValueAsString(policy);
        String encodedPolicy = Base64.encodeBase64String(jsonPolicy.getBytes());

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(accessKeySecret.getBytes(), "HmacSHA1"));
        byte[] signBytes = mac.doFinal(encodedPolicy.getBytes());
        String signature = Base64.encodeBase64String(signBytes);

        Map<String, String> resp = new HashMap<>();
        resp.put("host", "http://" + bucketName + "." + endpoint);
        resp.put("dir", dir);
        resp.put("policy", encodedPolicy);
        resp.put("signature", signature);
        resp.put("accessKeyId", accessKeyId);  // 必须返回！

        return R.ok(resp);
    }

    public static byte[] hmacsha256(byte[] key, String data) {
        try {
            // 初始化HMAC密钥规格，指定算法为HMAC-SHA256并使用提供的密钥。
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");

            // 获取Mac实例，并通过getInstance方法指定使用HMAC-SHA256算法。
            Mac mac = Mac.getInstance("HmacSHA256");
            // 使用密钥初始化Mac对象。
            mac.init(secretKeySpec);

            // 执行HMAC计算，通过doFinal方法接收需要计算的数据并返回计算结果的数组。
            byte[] hmacBytes = mac.doFinal(data.getBytes());

            return hmacBytes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }


    @GetMapping("/test")
    public R test() {
        System.out.println("收到请求！");
        return R.ok();
    }
}
