package com.sangui.sanguimall.thirdparty.oss.web;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sangui.sanguimall.result.R;
import com.sangui.sanguimall.thirdparty.oss.service.OssService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
        String url = ossService.getSignedUrl(uploadedImageUrl);
        return R.ok(url);
    }

    @DeleteMapping("/delete")
    public R deleteFile(@RequestParam(value = "objectName") String objectName) {
        String message = ossService.deleteFile(objectName);
        return R.ok(message);
    }

    @DeleteMapping("/batchDelete")
    public R deleteFiles(@RequestBody List<String> objectNames) {
        String message = ossService.deleteFiles(objectNames);
        return R.ok(message);
    }
}
