package com.dwl.service_edu.controller;


import com.dwl.common_utils.Result;
import com.dwl.service_edu.entity.vo.SubjectNestedVo;
import com.dwl.service_edu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-01-13
 */
@Api("课程分类管理")
@CrossOrigin
@RestController
@RequestMapping("/eduService/eduSubject")
public class EduSubjectController {

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EduSubjectController.class);

    /**
     * 课程科目服务类
     */
    private EduSubjectService eduSubjectService;

    /**
     * 无参构造器
     */
    public EduSubjectController() {
    }

    /**
     * 带参构造器
     *
     * @param eduSubjectService
     */
    @Autowired
    public EduSubjectController(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    /**
     * 添加课程分类
     *
     * @param file excel文件
     * @return Result
     */
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("/addSubject")
    public Result addSubject(
            @ApiParam(name = "file", value = "spring的文件上传类", required = true)
                    MultipartFile file) {
        try {
            eduSubjectService.importSubjectData(file, eduSubjectService);
            return Result.ok();
        } catch (Exception e) {
            LOGGER.error("添加课程分类异常：{}", e.getMessage());
            return Result.error().message("错误信息：" + e.getMessage());
        }
    }

    /**
     * 获取课程分类列表（树形）信息
     *
     * @return Result
     */
    @ApiOperation(value = "课程分类列表（树形）")
    @GetMapping("/nestedList")
    public Result nestedList() {
        try {
            List<SubjectNestedVo> subjectNestedVoList = eduSubjectService.nestedList();
            return Result.ok().data("items", subjectNestedVoList);
        } catch (Exception e) {
            LOGGER.error("获取课程分类列表（树形）信息异常：{}", e.getMessage());
            return Result.error().message("获取课程异常 :" + e.getMessage());
        }
    }

}

