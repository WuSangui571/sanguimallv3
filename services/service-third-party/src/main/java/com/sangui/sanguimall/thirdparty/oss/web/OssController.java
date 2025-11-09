package com.sangui.sanguimall.thirdparty.oss.web;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sangui.sanguimall.result.R;
import com.sangui.sanguimall.thirdparty.oss.service.OssService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.*;
import java.util.*;

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
    @Resource
    OssService ossService;

    @GetMapping("/getPolicy")
    public R getPostSignatureForOssUpload(@RequestParam(value = "dir")String dir) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Map<String, String> resp = ossService.getPolicy(dir);
        return R.ok(resp);
    }


    @GetMapping("/getSignedUrl")
    public R getSignedUrl(@RequestParam(value = "uploadedImageUrl") String uploadedImageUrl) {
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");
        String bucketName = "sanguimall-test";



        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 设置过期时间：300秒 （可配置）
        Date expiration = new Date(System.currentTimeMillis() + 300 * 1000L);
        URL url = ossClient.generatePresignedUrl(bucketName, uploadedImageUrl, expiration);
        ossClient.shutdown();
        System.out.println("后端返回的最终 url = "+ url.toString());
        return R.ok(url.toString());
    }
}
