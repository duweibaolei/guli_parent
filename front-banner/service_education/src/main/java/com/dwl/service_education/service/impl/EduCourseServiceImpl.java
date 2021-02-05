package com.dwl.service_education.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.Result.Result;
import com.dwl.service_education.client.impl.IEducationClient;
import com.dwl.service_education.entity.EduCourse;
import com.dwl.service_education.entity.query.CourseQuery;
import com.dwl.service_education.mapper.EduCourseMapper;
import com.dwl.service_education.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2021-02-05
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    private final IEducationClient client;

    @Autowired
    private EduCourseServiceImpl(IEducationClient client) {
        this.client = client;
    }

    @Override
    public Result courseQueryPage(long page, long limit, CourseQuery courseQuery) {
        return client.coursePageQuery(page, limit, courseQuery);
    }
}
