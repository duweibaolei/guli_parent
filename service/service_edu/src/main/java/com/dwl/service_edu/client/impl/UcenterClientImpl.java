package com.dwl.service_edu.client.impl;

import com.dwl.service_edu.client.UcenterClient;
import com.dwl.service_edu.entity.uc.UcenterMember;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {

    /**
     * 根据用户id获取用户信息
     *
     * @param memberId
     * @return
     */
    @Override
    public UcenterMember getUcenterPay(String memberId) {
        return null;
    }
}
