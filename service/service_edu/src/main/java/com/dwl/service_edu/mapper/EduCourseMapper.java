package com.dwl.service_edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.vo.CoursePublishVo;
import com.dwl.service_edu.entity.vo.frontvo.CourseWebVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author dwl
 * @since 2021-01-15
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    /**
     * 选择课程发布
     *
     * @param courseId
     * @return
     */
    CoursePublishVo selectCoursePublishVoById(String courseId);

    /**
     * 根据课程id查询课程信息
     *
     * @param courseId 课程id
     * @return 课程信息
     */
    CourseWebVo selectInfoWebById(String courseId);

}
