package com.dwl.service_vod.vodUtil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取去配置文件的实体类
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    /**
     * 阿里云oss链接地址
     */
    @Value("${aliyun.oss.file.endpoint}")
    private String endPoint;

    /**
     * 阿里云ossAccessKeyId
     */
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    /**
     * 阿里云oss密钥
     */
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    /**
     * 阿里云oss上传文件名称
     */
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() {
        END_POINT = this.endPoint;
        KEY_ID = this.keyId;
        KEY_SECRET = this.keySecret;
        BUCKET_NAME = this.bucketName;
    }
}
