package com.sangui.sanguimall.product;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;

import com.aliyun.oss.model.*;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-07
 * @Description: 测试 阿里云 OSS
 * @Version: 1.0
 */
@SpringBootTest
public class OssTest {
    /**
     * 测试初始化
     */
    @Test
    public void testInitOss(){
        // 从环境变量获取访问凭证
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        System.out.println(accessKeyId);
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");
        System.out.println(accessKeySecret);

        // 设置OSS地域和Endpoint
        String region = "cn-beijing";
        String endpoint = "oss-cn-beijing.aliyuncs.com";

        // 创建凭证提供者
        DefaultCredentialProvider provider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 配置客户端参数
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 显式声明使用V4签名算法
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        // 初始化OSS客户端
        OSS ossClient = OSSClientBuilder.create()
                .credentialsProvider(provider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .endpoint(endpoint)
                .build();

        // 列出当前用户的所有Bucket
        List<Bucket> buckets = ossClient.listBuckets();
        System.out.println("成功连接到 OSS 服务，当前账号下的 Bucket 列表：");

        if (buckets.isEmpty()) {
            System.out.println("当前账号下暂无 Bucket");
        } else {
            for (Bucket bucket : buckets) {
                System.out.println("- " + bucket.getName());
            }
        }

        // 释放资源
        ossClient.shutdown();
        System.out.println("OSS 客户端已关闭");
    }

    /**
     * 测试上传功能
     */
    @Test
    public void testUpload() throws Exception {
        // Endpoint 外网域名
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 从环境变量中获取访问凭证。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写 Bucket 名称
        String bucketName = "sanguimall-test";
        // 填写 Object 完整路径，完整路径中不能包含 Bucket 名称
        String objectName = "test/testImage.jpg";
        // 填写本地文件的完整路径，
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath= "D:\\01-TempFiles\\2025-11-07-upload\\testImag.jpg";
        // 填写 Bucket 所在地域
        String region = "cn-beijing";

        // 创建 OSSClient 实例。
        // 当 OSSClient 实例不再使用时，调用 shutdown 方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建 PutObjectRequest 对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建 PutObject 请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
