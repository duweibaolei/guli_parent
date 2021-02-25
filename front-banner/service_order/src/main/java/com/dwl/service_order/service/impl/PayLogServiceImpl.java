package com.dwl.service_order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.HttpClient;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_order.entity.Order;
import com.dwl.service_order.entity.PayLog;
import com.dwl.service_order.mapper.PayLogMapper;
import com.dwl.service_order.service.OrderService;
import com.dwl.service_order.service.PayLogService;
import com.dwl.service_order.util.WX;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2021-02-23
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    private OrderService orderService;

    @Autowired
    public PayLogServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    public PayLogServiceImpl() {
    }

    /**
     * 生成微信支付二维码接口
     *
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, Object> createNatvie(String orderNo) {
        try {
            // 1.根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);

            // 2.使用map设置生成二维码需要参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", WX.APP_ID.getStr());
            m.put("mch_id", WX.MCH_ID.getStr());
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle()); // 课程标题
            m.put("out_trade_no", orderNo); // 订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", WX.NOTIFY_URL.getStr() + "\n");
            m.put("trade_type", "NATIVE");

            // 3.发送httpclient请求，传递参数xml格式，微信支付提供的固定的地址
            HttpClient client = new HttpClient(WX.PAY_CLIENT.getStr());
            // 设置xml格式的参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, WX.KEY.getStr()));
            client.setHttps(true);
            // 执行post请求发送
            client.post();

            /* 4 得到发送请求返回结果
               返回内容，是使用xml格式返回 */
            String xml = client.getContent();

            // 把xml格式转换map集合，把map集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            // 最终返回数据 的封装
            Map<String, Object> map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));  // 返回二维码操作状态码
            map.put("code_url", resultMap.get("code_url"));        // 二维码地址

            return map;
        } catch (Exception e) {
            throw new GuLiException(ResultCode.ERROR.getStatus(), e + "");
        }

    }

    /**
     * 查询订单支付状态
     *
     * @param orderNo 订单号
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            // 1.封装参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", WX.APP_ID.getStr());
            m.put("mch_id", WX.MCH_ID.getStr());
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            // 2.发送httpclient
            HttpClient client = new HttpClient(WX.QUERY_CLIENT.getStr());
            client.setXmlParam(WXPayUtil.generateSignedXml(m, WX.KEY.getStr()));
            client.setHttps(true);
            client.post();

            // 3.得到请求返回内容
            String xml = client.getContent();
            // 6.转成Map再返回
            return WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 添加支付记录和更新订单状态
     *
     * @param map
     */
    @Override
    public void updateOrdersStatus(Map<String, String> map) {
        // 从map获取订单号
        String orderNo = map.get("out_trade_no");
        // 根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);

        // 更新订单表订单状态
        if (order.getStatus() == 1) {
            return;
        }
        order.setStatus(1);// 1代表已经支付
        orderService.updateById(order);

        // 向支付表添加支付记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);  // 订单号
        payLog.setPayTime(new Date()); // 订单完成时间
        payLog.setPayType(1);// 支付类型 1微信
        payLog.setTotalFee(order.getTotalFee());// 总金额(分)

        payLog.setTradeState(map.get("trade_state"));// 支付状态
        payLog.setTransactionId(map.get("transaction_id")); // 流水号
        payLog.setAttr(JSONObject.toJSONString(map));

        baseMapper.insert(payLog);
    }
}
