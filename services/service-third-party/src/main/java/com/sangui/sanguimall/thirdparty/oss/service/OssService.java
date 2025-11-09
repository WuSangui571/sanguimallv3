package com.sangui.sanguimall.thirdparty.oss.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-09
 * @Description:
 * @Version: 1.0
 */
public interface OssService {
    Map<String, String> getPolicy(String dir) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException;

    String getSignedUrl(String uploadedImageUrl);
}
