package com.sangui.sanguimall.thirdparty.oss.constant;


/**
 * @Author: sangui
 * @CreateTime: 2025-11-09
 * @Description:
 * @Version: 1.0
 */
public class OssConstants {
    public static final String BUCKET = "sanguimall-test";
    public static final String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    public static final String ACCESS_KEY_ID = System.getenv("OSS_ACCESS_KEY_ID");
    public static final String ACCESS_KEY_SECRET = System.getenv("OSS_ACCESS_KEY_SECRET");
}
