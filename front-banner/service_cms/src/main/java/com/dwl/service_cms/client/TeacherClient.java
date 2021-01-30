package com.dwl.service_cms.client;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_cms.client.impl.TeacherDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
/* 用于指定从哪个服务中调用功能 ，名称与被调用的服务名保持一致。采用熔断机制。*/
@FeignClient(name = "service-edu", fallback = TeacherDegradeFeignClient.class)
public interface TeacherClient {

    /**
     * 首页获取课程数据
     *
     * @param limit
     * @return
     */
    @GetMapping("/eduService/eduTeacher/listTeacher/{limit}")
    Result listTeacher(@PathVariable("limit") String limit);

}
