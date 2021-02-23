package com.dwl.service_order.service.impl;

import com.dwl.service_order.entity.PayLog;
import com.dwl.service_order.mapper.PayLogMapper;
import com.dwl.service_order.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
