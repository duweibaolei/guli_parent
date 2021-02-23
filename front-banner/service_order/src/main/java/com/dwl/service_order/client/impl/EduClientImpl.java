package com.dwl.service_order.client.impl;

import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.ordervo.CourseWebVoOrder;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_order.client.EduClient;
import org.springframework.stereotype.Component;

@Component
public class EduClientImpl implements EduClient {

    /**
     * 根据课程id查询课程信息
     *
     * @param id 课程id
     * @return
     */
    @Override
    public CourseWebVoOrder getCourseInfoOrder(String id) {
        throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "根据课程id查询课程信息异常！");
    }
}
