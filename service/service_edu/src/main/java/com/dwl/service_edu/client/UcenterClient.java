package com.dwl.service_edu.client;

import com.dwl.service_edu.client.impl.UcenterClientImpl;
import com.dwl.service_edu.entity.uc.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用登录类
 */
@Component
@FeignClient(name = "service-ucenter", fallback = UcenterClientImpl.class)
public interface UcenterClient {

    /**
     * 根据用户id获取用户信息
     *
     * @param memberId
     * @return
     */
    @GetMapping("/serviceUcEnter/ucEnterMember/getUserInfoOrder/{memberId}")
    UcenterMember getUcenterPay(@PathVariable("memberId") String memberId);
}
