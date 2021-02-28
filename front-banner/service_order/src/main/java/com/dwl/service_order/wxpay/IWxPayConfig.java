package com.dwl.service_order.wxpay;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 继承sdk WXPayConfig 实现sdk中部分抽象方法
 */
@Service
public class IWxPayConfig extends WXPayConfig {

    private final byte[] certData;

    private String app_id;

    private String wx_pay_key;

    private String wx_pay_mch_id;

    public IWxPayConfig() throws Exception { // 构造方法读取证书, 通过getCertStream 可以使sdk获取到证书
        String certPath = "/data/config/chidori/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    private void setAppID() {
        this.app_id = "wx74862e0dfcf69954";
    }

    private void setWXPayKey() {
        this.wx_pay_key = "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb";
    }

    private void setWXPayMchId() {
        this.wx_pay_mch_id = "1558950191";
    }

    @Override
    public String getAppID() {
        this.setAppID();
        return app_id;
    }

    @Override
    public String getMchID() {
        this.setWXPayMchId();
        return wx_pay_mch_id;
    }

    @Override
    public String getKey() {
        this.setWXPayKey();
        return wx_pay_key;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public IWXPayDomain getWXPayDomain() { // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }
}
