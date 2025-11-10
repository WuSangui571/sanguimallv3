package com.sangui.sanguimall.product.feign;


import com.sangui.sanguimall.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-09
 * @Description: 第三方服务的 Feign 客户端接口
 * @Version: 1.0
 */
@FeignClient("service-third-party")
public interface ThirdPartyFeignClient {
    @GetMapping("/oss/getSignedUrl")
    R getSignedUrl(@RequestParam(value = "uploadedImageUrl") String uploadedImageUrl);

    @DeleteMapping("/oss/delete")
    R deleteFile(@RequestParam(value = "objectName") String objectName);

    @DeleteMapping("/oss/batchDelete")
    R deleteFiles(@RequestBody List<String> objectNames);
}
