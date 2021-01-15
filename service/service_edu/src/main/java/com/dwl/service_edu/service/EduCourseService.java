package com.dwl.service_edu.service;

import com.dwl.service_edu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.vo.CourseInfoVo;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-01-15
 */
public interface EduCourseService extends IService<EduCourse> {

    /**
     * 保存课程信息
     * @param courseInfoVo
     */
    String saveCourseInfo(CourseInfoVo courseInfoVo);
}
