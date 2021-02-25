package com.dwl.service_order.client;

import com.dwl.common_utils.ordervo.CourseWebVoOrder;
import com.dwl.service_order.client.impl.EduClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "service-edu", fallback = EduClientImpl.class)
public interface EduClient {

    /**
     * 根据课程id查询课程信息
     * @param id 课程id
     * @return CourseWebVoOrder
     */
    @PostMapping("/eduService/front/courseFront/getCourseInfoOrder/{id}")
    CourseWebVoOrder getCourseInfoOrder(@PathVariable("id") String id);
}
