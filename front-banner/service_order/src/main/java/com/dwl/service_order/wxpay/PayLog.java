package com.dwl.service_order.wxpay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dwl.common_utils.util.MD5Util;
import com.dwl.service_order.entity.Order;
import com.dwl.service_order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayLog {

    public static Logger logger = LoggerFactory.getLogger(PayLog.class);

    private IWxPayConfig iWxPayConfig;

    private OrderService orderService;

    @Autowired
    public PayLog(IWxPayConfig iWxPayConfig, OrderService orderService) {
        this.iWxPayConfig = iWxPayConfig;
        this.orderService = orderService;
    }

    public PayLog() {
    }

    public Map<String, Object> createNatvie(String orderNo) {
        // 发起微信支付
        WXPay wxpay;
        Map<String, Object> result = new HashMap<>();
        try {
            // ******************************************
            //
            //  统一下单
            //
            // ******************************************
            wxpay = new WXPay(iWxPayConfig); // *** 注入自己实现的微信配置类, 创建WXPay核心类, WXPay 包括统一下单接口

            // 1.根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);

            Map<String, String> data = new HashMap<>();
            data.put("body", "订单详情");
            data.put("out_trade_no", orderNo); // 订单唯一编号, 不允许重复
            data.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + ""); // 订单金额, 单位分
            data.put("spbill_create_ip", "127.0.0.1"); // 下单ip
            data.put("openid", iWxPayConfig.getAppID()); // 微信公众号统一标示openid
            data.put("notify_url", "http://wxlj.oopmind.com/payCallback"); // 订单结果通知, 微信主动回调此接口
            data.put("trade_type", "JSAPI"); // 固定填写

            logger.info("发起微信支付下单接口, request={}", data);
            Map<String, String> response = wxpay.unifiedOrder(data); // 微信sdk集成方法, 统一下单接口unifiedOrder, 此处请求   MD5加密   加密方式
            logger.info("微信支付下单成功, 返回值 response={}", response);
            String returnCode = response.get("return_code");
            if (!"ok".equals(returnCode)) {
                return null;
            }
            String resultCode = response.get("result_code");
            if (!"ok".equals(resultCode)) {
                return null;
            }
            String prepay_id = response.get("prepay_id");
            if (prepay_id == null) {
                return null;
            }

            // ******************************************
            //
            //  前端调起微信支付必要参数
            //
            // ******************************************
            String packages = "prepay_id=" + prepay_id;
            Map<String, String> wxPayMap = new HashMap<>();
            wxPayMap.put("appId", iWxPayConfig.getAppID());
            wxPayMap.put("timeStamp", String.valueOf(new Date().getTime()));
            wxPayMap.put("nonceStr", String.valueOf(MD5Util.randomNumber()));
            wxPayMap.put("package", packages);
            wxPayMap.put("signType", "MD5");
            // 加密串中包括 appId timeStamp nonceStr package signType 5个参数, 通过sdk WXPayUtil类加密, 注意, 此处使用  MD5加密  方式
            String sign = WXPayUtil.generateSignature(wxPayMap, iWxPayConfig.getKey());

            // ******************************************
            //
            //  返回给前端调起微信支付的必要参数
            //
            // ******************************************
            result.put("prepay_id", prepay_id);
            result.put("sign", sign);

            result.putAll(wxPayMap);
            return result;
        } catch (Exception e) {

        }
        return null;
    }
}
