package com.dwl.service_order.client;

import com.dwl.common_utils.ordervo.UcenterMemberOrder;
import com.dwl.service_order.client.impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Component
@FeignClient(name = "service-ucenter", fallback = UcenterClientImpl.class)
public interface UcenterClient {

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return UcenterMemberOrder
     */
    @PostMapping("/serviceUcEnter/ucEnterMember/getUserInfoOrder/{id}")
    UcenterMemberOrder getUserInfoOrder(@PathVariable("id") String id);
}
