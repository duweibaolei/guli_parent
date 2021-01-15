package com.dwl.service_edu.controller;


import com.dwl.common_utils.BeanUtil;
import com.dwl.common_utils.Result;
import com.dwl.service_edu.entity.vo.CourseInfoVo;
import com.dwl.service_edu.service.EduCourseService;
import io.swagger.annotations.Api;
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
 * @since 2021-01-15
 */
@RestController
@RequestMapping("/eduService/eduCourse")
@CrossOrigin
@Api("课程管理")
public class EduCourseController {

    private EduCourseService eduCourseService;

    public EduCourseController() {
    }

    @Autowired
    public EduCourseController(EduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
    }

    @ApiOperation(value = "新增课程")
    @PostMapping("/saveCourseInfo")
    public Result saveCourseInfo(
            @ApiParam(name = "CourseInfoVo", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVo courseInfoVo) {
        if (BeanUtil.isEmpty(courseInfoVo)) {
            return Result.error().message("课程基本信息为空！");
        }
        try {
           String courseId = eduCourseService.saveCourseInfo(courseInfoVo);
            return Result.ok().data("courseId", courseId);
        } catch (Exception e) {
            return Result.error().message("发生错误信息：" + e.getMessage());
        }
    }
}

