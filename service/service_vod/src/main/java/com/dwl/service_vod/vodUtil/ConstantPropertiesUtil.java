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
     * 阿里云ossAccessKeyId
     */
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    /**
     * 阿里云oss密钥
     */
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    public static String KEY_ID;
    public static String KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID = this.keyId;
        KEY_SECRET = this.keySecret;
    }
}
