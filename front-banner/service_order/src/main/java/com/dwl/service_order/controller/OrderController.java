package com.dwl.service_order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.util.JwtUtil;
import com.dwl.service_order.entity.Order;
import com.dwl.service_order.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-02-23
 */
@RestController
@RequestMapping("/orderService/order")
@CrossOrigin
public class OrderController {

//    private OrderService orderService;
//
//    @Autowired
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//    public OrderController() {
//    }
//
//    /**
//     * @param courseId 课程id
//     * @param request  请求头
//     * @return Result
//     */
//    @ApiOperation(value = "生成订单的方法")
//    @PostMapping("createOrder/{courseId}")
//    public Result saveOrder(
//            @ApiParam(name = "courseId", value = "课程id", required = true)
//            @PathVariable String courseId,
//            HttpServletRequest request) {
//        // 创建订单，返回订单号
//        String orderNo =
//                orderService.createOrders(courseId, JwtUtil.getMemberIdByJwtToken(request));
//        return Result.ok().data("orderId", orderNo);
//    }

//    /**
//     * 根据订单id查询订单信息
//     *
//     * @param orderId 订单id
//     * @return Result
//     */
//    @ApiOperation(value = "根据订单id查询订单信息")
//    @GetMapping("getOrderInfo/{orderId}")
//    public Result getOrderInfo(
//            @ApiParam(name = "orderId", value = "订单id", required = true)
//            @PathVariable String orderId) {
//        QueryWrapper<Order> wrapper = new QueryWrapper<>();
//        wrapper.eq("order_no", orderId);
//        Order order = orderService.getOne(wrapper);
//        return Result.ok().data("item", order);
//    }
}

