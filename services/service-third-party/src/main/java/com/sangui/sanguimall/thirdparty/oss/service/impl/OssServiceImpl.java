package com.sangui.sanguimall.thirdparty.oss.service.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangui.sanguimall.thirdparty.oss.service.OssService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.sangui.sanguimall.thirdparty.oss.constant.OssConstants.*;
import static com.sangui.sanguimall.thirdparty.oss.util.OssUtils.generateExpiration;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-09
 * @Description:
 * @Version: 1.0
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public Map<String, String> getPolicy(String dir) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
//        String dir = "test06/";
        dir = dir.trim();

        if (ACCESS_KEY_ID == null || ACCESS_KEY_SECRET == null) {
            throw new RuntimeException("OSS 密钥未配置");
        }

        Map<String, Object> policy = new HashMap<>();
        policy.put("expiration", generateExpiration(300));

        List<Object> conditions = new ArrayList<>();
        conditions.add(Map.of("bucket", BUCKET));
        conditions.add(Arrays.asList("content-length-range", 1, 104857600));
        conditions.add(Arrays.asList("eq", "$success_action_status", "200"));
        conditions.add(Arrays.asList("starts-with", "$key", dir));

        policy.put("conditions", conditions);

        ObjectMapper mapper = new ObjectMapper();
        String jsonPolicy = mapper.writeValueAsString(policy);
        String encodedPolicy = org.apache.commons.codec.binary.Base64.encodeBase64String(jsonPolicy.getBytes());

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(ACCESS_KEY_SECRET.getBytes(), "HmacSHA1"));
        byte[] signBytes = mac.doFinal(encodedPolicy.getBytes());
        String signature = Base64.encodeBase64String(signBytes);

        Map<String, String> resp = new HashMap<>();
        resp.put("host", "http://" + BUCKET + "." + ENDPOINT);
        resp.put("dir", dir);
        resp.put("policy", encodedPolicy);
        resp.put("baseUrl", "https://" + BUCKET + "." + ENDPOINT);
        resp.put("signature", signature);
        resp.put("accessKeyId", ACCESS_KEY_ID);
        return resp;
    }

    @Override
    public String getSignedUrl(String uploadedImageUrl) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        // 设置过期时间：300秒 （可配置）
        Date expiration = new Date(System.currentTimeMillis() + 300 * 1000L);
        URL url = ossClient.generatePresignedUrl(BUCKET, uploadedImageUrl, expiration);
        ossClient.shutdown();
        System.out.println("后端返回的最终 url = " + url.toString());
        return url.toString();
    }

    @Override
    public String deleteFile(String objectName) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            ossClient.deleteObject(BUCKET, objectName);
            return "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public String deleteFiles(List<String> objectNames) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(BUCKET).withKeys(objectNames);
            ossClient.deleteObjects(deleteObjectsRequest);
            return "批量删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "批量删除失败，" + e.getMessage();
        } finally {
            ossClient.shutdown();
        }
    }
}
