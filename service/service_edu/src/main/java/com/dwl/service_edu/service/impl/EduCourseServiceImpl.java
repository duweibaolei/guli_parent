package com.dwl.service_edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.config.EduCommonStatus;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.EduCourseDescription;
import com.dwl.service_edu.entity.vo.CourseInfoVo;
import com.dwl.service_edu.mapper.EduCourseMapper;
import com.dwl.service_edu.service.EduCourseDescriptionService;
import com.dwl.service_edu.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2021-01-15
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    private EduCourseDescriptionService descriptionService;

    EduCourseServiceImpl() {
    }

    @Autowired
    EduCourseServiceImpl(EduCourseDescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }


    /**
     * 保存课程服务信息
     *
     * @param courseInfoVo
     */
    @Override
    @Transactional
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        // 保存课程基本信息
        EduCourse course = new EduCourse();
        course.setStatus(EduCommonStatus.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoVo, course);
        // 需要注意的是这个地方不可以使用save方法，因为主键策略会失效，如果要使用，需要自己设置主键信息
        int resultCourseInfo = baseMapper.insert(course);
        if (resultCourseInfo == 0) {
            throw new GuLiException(20001, "课程信息保存失败");
        }

        String courseId = course.getId();
        // 保存课程介绍信息
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseId);
        description.setDescription(courseInfoVo.getDescription());
        boolean resultCourseDescriptionInfo = descriptionService.save(description);
        if (!resultCourseDescriptionInfo) {
            throw new GuLiException(20001, "课程信息保存失败");
        }
        return courseId;
    }
}
