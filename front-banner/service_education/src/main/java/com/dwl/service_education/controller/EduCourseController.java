package com.dwl.service_education.controller;


import com.dwl.common_utils.Result.Result;
import com.dwl.service_education.entity.query.CourseQuery;
import com.dwl.service_education.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-02-05
 */
@RestController
@RequestMapping("/serviceEducation/eduCourse")
public class EduCourseController {

    /**
     * 课程基本信息服务类
     */
    private final EduCourseService eduCourseService;

    @Autowired
    public EduCourseController(EduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
    }

    /**
     * 课程列表分页查询
     *
     * @param page
     * @param limit
     * @param courseQuery
     * @return
     */
    @ApiOperation(value = "课程列表分页查询")
    @PostMapping("/queryCoursePage/{page}/{limit}")
    public Result coursePageQuery(
            @ApiParam(name = "page", value = "当前页", required = true) @PathVariable long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit,
            @ApiParam(name = "course", value = "课程列表查询实体类") @RequestBody CourseQuery courseQuery) {
        return eduCourseService.courseQueryPage(page, limit, courseQuery);
    }
}

