package com.dwl.service_cms.client.impl;

import com.dwl.common_utils.Result;
import com.dwl.service_cms.client.CourseClient;
import org.springframework.stereotype.Component;

@Component
public class CourseDegradeFeignClient implements CourseClient {

    @Override
    public Result listCourse(String limit) {
        return Result.error().message("获取课程列表异常！");
    }
}
