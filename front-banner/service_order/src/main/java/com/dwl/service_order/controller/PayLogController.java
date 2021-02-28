package com.dwl.service_order.controller;


import com.dwl.common_utils.Result.Result;
import com.dwl.service_order.service.PayLogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-02-23
 */
@RestController
@RequestMapping("/orderService/payLog")
public class PayLogController {

    private PayLogService payLogService;

    @Autowired
    public PayLogController(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    public PayLogController() {
    }

    /**
     * 生成微信支付二维码接口
     *
     * @param orderNo 参数是订单号
     * @return
     */
    @ApiOperation(value = "生成微信支付二维码接口,参数是订单号")
    @GetMapping("/createNative/{orderNo}")
    public Result createNative(
            @ApiParam(name = "orderNo", value = "订单号", required = true)
            @PathVariable String orderNo) {
        //返回信息，包含二维码地址，还有其他需要的信息
        Map<String, Object> map = payLogService.createNatvie(orderNo);
        return Result.ok().data(map);
    }

    /**
     * 查询订单支付状态
     *
     * @param orderNo 订单号，根据订单号查询 支付状态
     * @return
     */
    @ApiOperation(value = "查询订单支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单号", required = true)
            @PathVariable String orderNo) {
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        if (map == null) {
            return Result.error().message("支付出错了");
        }
        // 如果返回map里面不为空，通过map获取订单状态
        if (map.get("trade_state").equals("SUCCESS")) {//支付成功
            // 添加记录到支付表，更新订单表订单状态
            payLogService.updateOrdersStatus(map);
            return Result.ok().message("支付成功");
        }
        return Result.ok().success(false).message("");
    }
}

