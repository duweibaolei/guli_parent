package com.dwl.service_cms.service.impl;

import com.dwl.common_utils.Result;
import com.dwl.service_cms.client.CourseClient;
import com.dwl.service_cms.client.TeacherClient;
import com.dwl.service_cms.controller.CrmBannerController;
import com.dwl.service_cms.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    @Override
    public Map<String, Object> getTeacherIndex() {
        return teacherClient.listTeacher("4").getData();
    }

    @Override
    public Map<String, Object>  getCourseIndex() {
        return courseClient.listCourse("8").getData();
    }
}
