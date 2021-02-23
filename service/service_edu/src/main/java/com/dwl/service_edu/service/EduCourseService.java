package com.dwl.service_edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.CourseQuery;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.vo.CourseInfoVo;
import com.dwl.service_edu.entity.vo.CoursePublishVo;
import com.dwl.service_edu.entity.vo.frontvo.CourseWebVo;
import com.dwl.service_edu.entity.vo.frontvo.FrontCourseQueryVo;

import java.util.List;
import java.util.Map;

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

    /**
     * 课程列表分页查询
     *
     * @param courseQuery
     * @param queryPage
     */
    void courseQueryPage(CourseQuery courseQuery, Page<EduCourse> queryPage);

    /**
     * 根据ID删除课程
     *
     * @param id
     */
    void removeCourseById(String id);

    /**
     * 获取指定的前几个课程
     *
     * @param limit
     * @return
     */
    List<EduCourse> getCourseList(String limit);

    /**
     * 通过讲师id获取讲师的相关课程信息
     *
     * @param id 讲师id
     * @return 课程集合
     */
    List<EduCourse> getByTeacherId(String id);

    /**
     * 前端获取课程分页列表
     *
     * @param current 当前页
     * @param limit   每页记录数
     * @param queryVo
     * @return Map<String, Object>
     */
    Map<String, Object> pageListWeb(long current, long limit, FrontCourseQueryVo queryVo);

    /**
     * 根据课程id查询课程信息
     *
     * @param courseId
     * @return
     */
    CourseWebVo getBaseCourseInfo(String courseId);

    /**
     * 更新课程浏览数
     *
     * @param id
     */
    void updatePageViewCount(String id);

}
