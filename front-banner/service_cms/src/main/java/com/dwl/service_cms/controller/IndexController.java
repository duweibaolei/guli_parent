package com.dwl.service_cms.controller;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_cms.service.IndexService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms/index")
@CrossOrigin
public class IndexController {
    /**
     * 日志服务
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CrmBannerController.class);

    /**
     * 课程服务类
     */
    private final IndexService indexService;

    @Autowired
    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 获取首页的课程和讲师
     *
     * @return Result
     */
    @ApiOperation("获取首页的课程和讲师")
    @GetMapping("/indexTeacherAndCourse")
    public Result indexTeacherAndCourse() {
        List<Object> list = new ArrayList<>();
        try {
            list.add(indexService.getCourseIndex("8"));
            list.add(indexService.getTeacherIndex("8"));
            return Result.ok().data("courseList", list.get(0)).data("teacherList", list.get(1));
        } catch (Exception e) {
            LOGGER.error("获取首页的课程和讲师数据异常：{}", e + "");
            return Result.error().message("获取首页的课程和讲师数据异常：" + e);
        }
    }

}
