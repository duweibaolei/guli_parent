package com.dwl.service_edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.vo.CourseInfoVo;
import com.dwl.service_edu.entity.vo.CoursePublishVo;

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

    /**
     * 选择课程发布
     *
     * @param id
     * @return
     */
    CoursePublishVo getCoursePublishVoById(String id);

    /**
     * 根据id发布课程
     *
     * @param id
     * @return
     */
    boolean publishCourseById(String id);
}
