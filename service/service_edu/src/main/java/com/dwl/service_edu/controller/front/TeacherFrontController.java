package com.dwl.service_edu.controller.front;

import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.util.BeanUtil;
import com.dwl.service_edu.entity.EduCourse;
import com.dwl.service_edu.entity.EduTeacher;
import com.dwl.service_edu.service.EduCourseService;
import com.dwl.service_edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "讲师管理")
@CrossOrigin // 跨域
@RestController
@RequestMapping("/eduService/front/teacherFront")
public class TeacherFrontController {

    /**
     * 讲师 服务类
     */
    private EduTeacherService eduTeacherService;

    /**
     * 课程基本信息服务类
     */
    private EduCourseService eduCourseService;

    public TeacherFrontController() {
    }

    @Autowired
    public TeacherFrontController(EduTeacherService eduTeacherService, EduCourseService eduCourseService) {
        this.eduTeacherService = eduTeacherService;
        this.eduCourseService = eduCourseService;
    }

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherFrontController.class);

    /**
     * 分页查询讲师的方法
     *
     * @param current 当前页
     * @param limit   每页记录数
     * @return Result
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public Result pageListTeacher(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit) {
        Map<String, Object> map = eduTeacherService.pageListWeb(current, limit);
        return Result.ok().data(map);
    }


    /**
     * 根据讲师ID查询数据信息
     *
     * @param id 讲师id
     * @return 讲师信息和讲师课程相关信息
     */
    @ApiOperation(value = "根据讲师ID查询数据信息")
    @GetMapping("/getById/{id}")
    public Result getById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        List<EduCourse> courseList = eduCourseService.getByTeacherId(id);
        if (BeanUtil.isEmpty(eduTeacher)) {
            LOGGER.error("根据讲师ID查询讲师数据为空，讲师ID：{}", id);
            return Result.error().message("根据讲师ID查询讲师数据为空，请检查数据是否正常！");
        }
        return Result.ok().data("teacher", eduTeacher).data("courseList", courseList);
    }

}
