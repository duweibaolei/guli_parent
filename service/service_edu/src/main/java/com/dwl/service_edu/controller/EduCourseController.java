package com.dwl.service_edu.controller;


import com.dwl.common_utils.BeanUtil;
import com.dwl.common_utils.Result;
import com.dwl.common_utils.StringUtil;
import com.dwl.service_edu.entity.vo.CourseInfoVo;
import com.dwl.service_edu.entity.vo.CoursePublishVo;
import com.dwl.service_edu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EduCourseController.class);

    /**
     * 课程基本信息服务类
     */
    private EduCourseService eduCourseService;

    public EduCourseController() {
    }

    @Autowired
    public EduCourseController(EduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
    }

    /**
     * 新增课程基本相关信息
     *
     * @param courseInfoVo
     * @return
     */
    @ApiOperation(value = "新增课程")
    @PostMapping("/saveOrUpdateCourseInfo")
    public Result saveOrUpdateCourseInfo(
            @ApiParam(name = "CourseInfoVo", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVo courseInfoVo) {
        if (BeanUtil.isEmpty(courseInfoVo)) {
            return Result.error().message("课程基本信息为空！");
        }
        try {
            String courseId = eduCourseService.saveOrUpdateCourseInfo(courseInfoVo);
            return Result.ok().data("courseId", courseId);
        } catch (Exception e) {
            LOGGER.error("新增课程发生异常：相关异常信息 [{}]", e.getMessage());
            return Result.error().message("发生错误信息：" + e.getMessage());
        }
    }

    /**
     * 根据id获取课程的基本信息
     *
     * @param id 课程基本信息id
     * @return Result
     */
    @ApiOperation(value = "根据id获取课程的基本信息")
    @GetMapping("/getCourseInfoById/{id}")
    public Result getCourseInfoById(
            @ApiParam(name = "id", value = "课程信息ID", required = true) @PathVariable String id) {
        if (StringUtil.isEmpty(id)) {
            return Result.error().message("课程基本信息id为空！");
        }
        try {
            CourseInfoVo infoVo = eduCourseService.getCourseInfo(id);
            return Result.ok().data("infoVo", infoVo);
        } catch (Exception e) {
            LOGGER.error("课程的ID为：{}，课程的基本信息异常：{}", id, e.getMessage());
            return Result.error().message("获取课程的基本信息异常：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取课程发布信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID获取课程发布信息")
    @GetMapping("coursePublishInfo/{id}")
    public Result getCoursePublishVoById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id) {
        CoursePublishVo courseInfoForm = eduCourseService.getCoursePublishVoById(id);
        return Result.ok().data("item", courseInfoForm);
    }

    /**
     * 根据id发布课程
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publish-course/{id}")
    public Result publishCourseById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id) {
        eduCourseService.publishCourseById(id);
        return Result.ok();
    }
}

