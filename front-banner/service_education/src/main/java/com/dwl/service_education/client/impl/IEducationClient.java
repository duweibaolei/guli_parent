package com.dwl.service_education.client.impl;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_education.client.EducationFileDegradeFeignClient;
import com.dwl.service_education.entity.query.CourseQuery;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 调用课程相关的服务
 */
@Component
/* 用于指定从哪个服务中调用功能 ，名称与被调用的服务名保持一致。采用熔断机制。*/
@FeignClient(name = "service-edu", fallback = EducationFileDegradeFeignClient.class)
public interface IEducationClient {

    @PostMapping("/eduService/eduCourse/queryCoursePage/{page}/{limit}")
    Result coursePageQuery(
            @ApiParam(name = "page", value = "当前页", required = true) @PathVariable long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit,
            @ApiParam(name = "course", value = "课程列表查询实体类") @RequestBody CourseQuery courseQuery);

}
