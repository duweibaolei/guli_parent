package com.dwl.service_order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_order.entity.Order;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-02-23
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     *
     * @param courseId
     * @param memberIdByJwtToken
     * @return
     */
    String createOrders(String courseId, String memberIdByJwtToken);
}
