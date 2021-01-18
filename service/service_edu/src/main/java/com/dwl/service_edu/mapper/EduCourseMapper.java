package com.dwl.service_edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.vo.CoursePublishVo;

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
}
