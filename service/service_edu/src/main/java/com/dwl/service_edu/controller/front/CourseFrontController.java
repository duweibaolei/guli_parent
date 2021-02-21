package com.dwl.service_edu.controller.front;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_edu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "前端课程管理")
@RequestMapping("/eduService/front/courseFront")
@RestController
@CrossOrigin
public class CourseFrontController {

    /**
     * 课程基本信息服务类
     */
    private EduCourseService eduCourseService;

    public CourseFrontController() {
    }

    @Autowired
    public CourseFrontController(EduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
    }

    /**
     * 分页查询讲师的方法
     *
     * @param current 当前页
     * @param limit   每页记录数
     * @return Result
     */
    @ApiOperation(value = "分页课程列表")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public Result pageListTeacher(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit) {
        Map<String, Object> map = eduCourseService.pageListWeb(current, limit);
        return Result.ok().data(map);
    }
}
