package com.dwl.service_edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.EduCourse;
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
     *
     * @param courseInfoVo
     */
    String saveOrUpdateCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据id获取课程的基本信息
     *
     * @param id
     * @return
     */
    CourseInfoVo getCourseInfo(String id);
}
