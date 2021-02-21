package com.dwl.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.BeanUtil;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.client.VodClient;
import com.dwl.service_edu.config.EduCommonStatus;
import com.dwl.service_edu.entity.CourseQuery;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.EduCourseDescription;
import com.dwl.service_edu.entity.EduVideo;
import com.dwl.service_edu.entity.vo.CourseInfoVo;
import com.dwl.service_edu.entity.vo.CoursePublishVo;
import com.dwl.service_edu.mapper.EduCourseMapper;
import com.dwl.service_edu.service.EduChapterService;
import com.dwl.service_edu.service.EduCourseDescriptionService;
import com.dwl.service_edu.service.EduCourseService;
import com.dwl.service_edu.service.EduVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /**
     * 课程课时服务类
     */
    private EduVideoService videoService;
    /**
     * 课程章节服务类
     */
    private EduChapterService chapterService;

    EduCourseService eduCourseService;

    private VodClient vodClient;

    EduCourseServiceImpl() {
    }

    @Autowired
    EduCourseServiceImpl(EduCourseDescriptionService descriptionService, EduVideoService videoService,
                         EduChapterService chapterService, VodClient vodClient) {
        this.descriptionService = descriptionService;
        this.videoService = videoService;
        this.chapterService = chapterService;
        this.vodClient = vodClient;
    }


    /**
     * 保存课程服务信息
     *
     * @param courseInfoVo
     */
    @Override
    @Transactional
    @CacheEvict(value = "courseList", allEntries = true)
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
                throw new GuLiException(ResultCode.UPDATE_ERROR.getStatus(), "课程信息更新失败");
            }
            // 更新课程的简介信息
            flag = descriptionService.updateById(description);
            if (!flag) {
                LOGGER.error("课程介绍信息更新异常，课程介绍信息ID为：{}", description.getId());
                throw new GuLiException(ResultCode.UPDATE_ERROR.getStatus(), "课程介绍保存失败");
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

    /**
     * 选择课程发布
     *
     * @param id
     * @return
     */
    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    /**
     * 根据id发布课程
     *
     * @param id
     * @return
     */
    @Override
    public boolean publishCourseById(String id) {
        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus(EduCommonStatus.COURSE_NORMAL);
        Integer count = baseMapper.updateById(course);
        return null != count && count > 0;
    }

    /**
     * 课程列表分页查询
     *
     * @param courseQuery
     * @param queryPage
     */
    @Override
    public void courseQueryPage(CourseQuery courseQuery, Page<EduCourse> queryPage) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");

        // 没有查询内容则直接查询
        if (BeanUtil.isEmpty(courseQuery)) {
            baseMapper.selectPage(queryPage, queryWrapper);
            return;
        }
        // 获取数据信息
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        // 拼装数据信息
        if (StringUtil.isNotEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (StringUtil.isNotEmpty(teacherId)) {
            queryWrapper.eq("teacher_id", teacherId);
        }
        if (StringUtil.isNotEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }
        if (StringUtil.isNotEmpty(subjectId)) {
            queryWrapper.eq("subject_id", subjectId);
        }
        // 条件查询
        baseMapper.selectPage(queryPage, queryWrapper);
    }

    /**
     * 根据ID删除课程相关信息
     *
     * @param id
     */
    @Override
    @Transactional
    @CacheEvict(value = "courseList", allEntries = true)
    public void removeCourseById(String id) {
        // 查询有多少视频存在云端
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoQueryWrapper.select("video_source_id");
        List<EduVideo> videoList = videoService.list(videoQueryWrapper);
        // 获取到所有的云端相关的视频id
        List<String> videoIdList = new ArrayList<>();
        for (EduVideo video : videoList) {
            videoIdList.add(video.getVideoSourceId());
        }
        // 调用阿里云服务删除视频
        vodClient.removeVideoList(videoIdList);
        // 根据id删除所有视频相关信息
        videoService.removeByCourseId(id);
        // 根据id删除所有章节
        chapterService.removeByCourseId(id);
        // 最后删除课程
        baseMapper.deleteById(id);
    }

    /**
     * 获取指定的前几个课程
     *
     * @param limit
     * @return
     */
    @Override
    public List<EduCourse> getCourseList(String limit) {
        // 查询前几条课程
        QueryWrapper<EduCourse> wrapperCourse = new QueryWrapper<>();
        wrapperCourse.orderByDesc("buy_count");
        wrapperCourse.last("limit " + limit);
        return baseMapper.selectList(wrapperCourse);
    }

    /**
     * 通过讲师id获取讲师的相关课程信息
     *
     * @param id 讲师id
     * @return 课程集合
     */
    @Override
    public List<EduCourse> getByTeacherId(String id) {
        if (StringUtil.isEmpty(id)) {
            return null;
        }
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id", id);
        courseQueryWrapper.orderByDesc("gmt_modified");
        return list(courseQueryWrapper);
    }

    /**
     * 前端获取讲师分页列表
     *
     * @param current 当前页
     * @param limit   每页记录数
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> pageListWeb(long current, long limit) {
        Page<EduCourse> coursePage = new Page<>(current, limit);
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("view_count");
        baseMapper.selectPage(coursePage, courseQueryWrapper);

        // 把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", coursePage.getRecords());
        map.put("current", coursePage.getCurrent());
        map.put("pages", coursePage.getPages());
        map.put("size", coursePage.getSize());
        map.put("total", coursePage.getTotal());
        map.put("hasNext", coursePage.hasNext());
        map.put("hasPrevious", coursePage.hasPrevious());

        // map返回
        return map;
    }
}
