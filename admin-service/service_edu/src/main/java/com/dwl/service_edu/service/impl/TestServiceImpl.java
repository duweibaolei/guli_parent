package com.dwl.service_edu.service.impl;

import com.dwl.service_edu.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String test() {
        return "测试";
    }
}
