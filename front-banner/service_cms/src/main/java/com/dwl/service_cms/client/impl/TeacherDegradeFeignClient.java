package com.dwl.service_cms.client.impl;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_cms.client.TeacherClient;
import org.springframework.stereotype.Component;

@Component
public class TeacherDegradeFeignClient implements TeacherClient {

    @Override
    public Result listTeacher(String limit) {
        return Result.error().message("获取名师列表异常！");
    }

}
