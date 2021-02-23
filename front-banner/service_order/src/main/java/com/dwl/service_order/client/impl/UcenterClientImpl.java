package com.dwl.service_order.client.impl;

import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.ordervo.UcenterMemberOrder;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_order.client.UcenterClient;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return
     */
    @Override
    public UcenterMemberOrder getUserInfoOrder(String id) {
        throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "根据用户id获取用户信息异常！");
    }
}
