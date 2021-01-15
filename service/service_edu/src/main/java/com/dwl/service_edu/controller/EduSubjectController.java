package com.dwl.service_edu.controller;


import com.dwl.common_utils.Result;
import com.dwl.service_edu.entity.vo.SubjectNestedVo;
import com.dwl.service_edu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
     * @param file
     * @return
     */
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("/addSubject")
    public Result addSubject(MultipartFile file) {
        try {
            eduSubjectService.importSubjectData(file, eduSubjectService);
            return Result.ok();
        } catch (Exception e) {
            return Result.error().data("错误信息：", e.getMessage());
        }
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("/nestedList")
    public Result nestedList(){
        List<SubjectNestedVo> subjectNestedVoList = eduSubjectService.nestedList();

        return Result.ok().data("items", subjectNestedVoList);
    }
}

