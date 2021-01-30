package com.dwl.service_cms.service.impl;

import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_cms.client.CourseClient;
import com.dwl.service_cms.client.TeacherClient;
import com.dwl.service_cms.controller.CrmBannerController;
import com.dwl.service_cms.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    /**
     * 日志服务
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CrmBannerController.class);

    /**
     * 课程服务类
     */
    private CourseClient courseClient;

    /**
     * 老师服务类
     */
    private TeacherClient teacherClient;


    public IndexServiceImpl(CourseClient courseClient, TeacherClient teacherClient) {
        this.courseClient = courseClient;
        this.teacherClient = teacherClient;
    }

    /**
     * 获取头几位讲师
     *
     * @param limit
     * @return
     */
    @Override
    @Cacheable(value = "teacherList", key = "'getCourseIndex'")
    public List<Object> getTeacherIndex(String limit) {
        List<Object> list = new ArrayList<>();
        Result result = teacherClient.listTeacher("4");
        if (!result.getSuccess()) {
            LOGGER.error("首页数据获取讲师信息异常：{}", result.getMessage());
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), result.getMessage());
        }
        Map<String, Object> map = result.getData();
        for (Map.Entry<String, Object> teacherMap : map.entrySet()) {
            list = (List<Object>) teacherMap.getValue();
        }
        return list;
    }

    /**
     * 获取头几们课程
     *
     * @param limit
     * @return
     */
    @Override
    @Cacheable(value = "courseList", key = "'getCourseIndex'")
    public List<Object> getCourseIndex(String limit) {
        List<Object> list = new ArrayList<>();
        Result result = courseClient.listCourse(limit);
        if (!result.getSuccess()) {
            LOGGER.error("首页数据获取课程信息异常：{}", result.getMessage());
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), result.getMessage());
        }
        Map<String, Object> map = result.getData();
        for (Map.Entry<String, Object> courseMap : map.entrySet()) {
            list = (List<Object>) courseMap.getValue();
        }
        return list;
    }

}
