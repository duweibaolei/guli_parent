package com.dwl.service_order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_order.entity.PayLog;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-02-23
 */
public interface PayLogService extends IService<PayLog> {

    //生成微信支付二维码接口

    /**
     * 生成微信支付二维码接口
     *
     * @param orderNo
     * @return Map
     */
    Map<String, Object> createNatvie(String orderNo);

    /**
     * 根据订单号查询订单支付状态
     *
     * @param orderNo 订单号
     * @return
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 向支付表添加记录，更新订单状态
     *
     * @param map
     */
    void updateOrdersStatus(Map<String, String> map);
}
