package com.dwl.service_education.service;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_education.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_education.entity.query.CourseQuery;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-02-05
 */
public interface EduCourseService extends IService<EduCourse> {

    Result courseQueryPage(long page, long limit, CourseQuery courseQuery);
}
