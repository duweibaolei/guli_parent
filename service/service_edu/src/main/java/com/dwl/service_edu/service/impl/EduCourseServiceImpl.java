package com.dwl.service_edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.BeanUtil;
import com.dwl.common_utils.ResultCode;
import com.dwl.common_utils.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.config.EduCommonStatus;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.EduCourseDescription;
import com.dwl.service_edu.entity.vo.CourseInfoVo;
import com.dwl.service_edu.mapper.EduCourseMapper;
import com.dwl.service_edu.service.EduCourseDescriptionService;
import com.dwl.service_edu.service.EduCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EduCourseServiceImpl.class);

    /**
     * 课程简介服务类
     */
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
    public String saveOrUpdateCourseInfo(CourseInfoVo courseInfoVo) {
        String courseId = null;

        // 课程的基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, course);
        // 课程的简介信息
        EduCourseDescription description = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, description);

        // 没有id则进行保存
        if (StringUtil.isEmpty(courseInfoVo.getId())) {
            // 保存课程基本信息
            course.setStatus(EduCommonStatus.COURSE_DRAFT);
            // 需要注意的是这个地方不可以使用save方法，因为主键策略会失效，如果要使用，需要自己设置主键信息
            int resultCourseInfo = baseMapper.insert(course);
            courseId = course.getId();
            description.setId(courseId);
            if (resultCourseInfo == 0) {
                LOGGER.error("课程基本信息保存异常，课程基本信息ID为：{}", course.getId());
                throw new GuLiException(ResultCode.SAVE_ERROR.getStatus(), "课程信息保存失败");
            }
            // 保存课程介绍信息
            boolean resultCourseDescriptionInfo = descriptionService.save(description);
            if (!resultCourseDescriptionInfo) {
                LOGGER.error("课程介绍信息保存异常，课程介绍信息ID为：{}", description.getId());
                throw new GuLiException(ResultCode.SAVE_ERROR.getStatus(), "课程简介保存失败");
            }
        } else {
            courseId = courseInfoVo.getId();
            // 更新课程的基本信息
            boolean flag = this.updateById(course);
            if (!flag) {
                LOGGER.error("课程基本信息更新异常，课程基本信息ID为：{}", course.getId());
                throw new GuLiException(ResultCode.UPDATA_ERROR.getStatus(), "课程信息更新失败");
            }
            // 更新课程的简介信息
            flag = descriptionService.updateById(description);
            if (!flag) {
                LOGGER.error("课程介绍信息更新异常，课程介绍信息ID为：{}", description.getId());
                throw new GuLiException(ResultCode.UPDATA_ERROR.getStatus(), "课程介绍保存失败");
            }
        }
        return courseId;
    }

    /**
     * 根据id获取课程的基本信息
     *
     * @param id
     * @return
     */
    @Override
    public CourseInfoVo getCourseInfo(String id) {
        CourseInfoVo infoVo = new CourseInfoVo();
        // 获取课程的基本信息
        EduCourse course = this.getById(id);
        if (BeanUtil.isEmpty(course)) {
            LOGGER.error("获取课程基本信息异常，课程的ID为：{}，", id);
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "获取课程基本信息异常！");
        }
        BeanUtils.copyProperties(course, infoVo);

        // 查询课程的简介信息
        EduCourseDescription description = descriptionService.getById(id);
        if (BeanUtil.isEmpty(course)) {
            LOGGER.error("获取课程简介信息异常，课程的ID为：{}，", id);
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "获取课程简介信息异常！");
        }
        BeanUtils.copyProperties(description, infoVo);
        return infoVo;
    }
}
