package com.dwl.service_education.client;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_education.client.impl.IEducationClient;
import com.dwl.service_education.entity.query.CourseQuery;
import org.springframework.stereotype.Component;

@Component
public class EducationFileDegradeFeignClient implements IEducationClient {


    @Override
    public Result coursePageQuery(long page, long limit, CourseQuery courseQuery) {
        return Result.error().message("课程列表分页查询出错了!");
    }
}
