package com.dwl.service_order.controller;

import com.dwl.service_order.wxpay.IWxPayConfig;
import com.dwl.service_order.wxpay.WXPay;
import com.dwl.service_order.wxpay.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

@RequestMapping("/orderService/payCallback")
@RestController
public class PayCallback {

    private IWxPayConfig iWxPayConfig;

    public static Logger logger = LoggerFactory.getLogger(PayCallback.class);

    @Autowired
    public PayCallback(IWxPayConfig iWxPayConfig) {
        this.iWxPayConfig = iWxPayConfig;
    }

    public PayCallback() {
    }

    @PostMapping("/payCallback")
    public String payCallback(HttpServletRequest request, HttpServletResponse response) {
        logger.info("进入微信支付异步通知");
        String resXml = "";
        try {
            InputStream is = request.getInputStream();
            // 将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            logger.info("微信支付异步通知请求包: {}", resXml);
            return this.payBack(resXml);
        } catch (Exception e) {
            logger.error("微信支付回调通知失败", e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }

    /**
     * 统一下单的签名和后续前端拉取微信支付的签名需要统一,
     * 也就是都采用MD5加密, 如果2者不同, 会导致前端拉取微信支付fail,
     * 这是一个巨大的坑, 因为这个原因调试了好久,
     * 微信在文档里没有明确标出统一下单的签名校验方式 需要和前端拉取微信支付的签名校验保持一致.
     * 微信sdk里的源码需要针对这个问题调整一下, 调整如下: WXPay类需要修改下加密判断，在WXPay构造方法中
     *
     * @param notifyData
     * @return
     */
    private String payBack(String notifyData) {
        logger.info("payBack() start, notifyData={}", notifyData);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            WXPay wxpay = new WXPay(iWxPayConfig);

            notifyMap = WXPayUtil.xmlToMap(notifyData);         // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//订单号

                if (out_trade_no == null) {
                    logger.info("微信支付回调失败订单号: {}", notifyMap);
                    xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    return xmlBack;
                }

                // 业务逻辑处理 ****************************
                logger.info("微信支付回调成功订单号: {}", notifyMap);
                xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[SUCCESS]]></return_msg>" + "</xml> ";
                return xmlBack;
            } else {
                logger.error("微信支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            logger.error("微信支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }

}
