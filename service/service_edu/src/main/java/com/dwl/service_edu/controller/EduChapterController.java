package com.dwl.service_edu.controller;


import com.dwl.common_utils.Result.Result;
import com.dwl.service_edu.entity.EduChapter;
import com.dwl.service_edu.entity.vo.ChapterNestedVo;
import com.dwl.service_edu.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-01-18
 */
@RestController
@Api(tags = "章节管理")
@CrossOrigin
@RequestMapping("/eduService/eduChapter")
public class EduChapterController {

    /**
     * 日志信息
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(EduChapterController.class);

    /**
     * 课程章节服务类
     */
    private EduChapterService eduChapterService;

    public EduChapterController() {
    }

    @Autowired
    public EduChapterController(EduChapterService chapterService) {
        this.eduChapterService = chapterService;
    }

    /**
     * 获取课程下的所有章节和课时信息（树形）
     *
     * @return Result
     */
    @ApiOperation("获取课程下的所有章节和课时信息（树形）")
    @GetMapping("/acquireChapter/{courseId}")
    public Result acquireChapterInfo(@ApiParam(name = "courseId", value = "课程id", required = true) @PathVariable String courseId) {
        try {
            List<ChapterNestedVo> nestedVoList = eduChapterService.nestedChapterList(courseId);
            return Result.ok().data("chapterNestedList", nestedVoList);
        } catch (Exception e) {
            return Result.error().message("查询章节信息异常：" + e.getMessage());
        }
    }

    /**
     * 新增章节
     *
     * @param chapter
     * @return
     */
    @ApiOperation(value = "新增章节")
    @PostMapping("/saveChapter")
    public Result saveChapter(
            @ApiParam(name = "chapterVo", value = "章节对象", required = true)
            @RequestBody EduChapter chapter) {
        eduChapterService.save(chapter);
        return Result.ok();
    }

    /**
     * 根据id获取章节信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("/getChapterById/{id}")
    public Result getById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id) {
        EduChapter chapter = eduChapterService.getById(id);
        return Result.ok().data("item", chapter);
    }

    /**
     * 根据ID修改章节
     *
     * @param id
     * @param chapter
     * @return
     */
    @ApiOperation(value = "根据ID修改章节")
    @PutMapping("/updateChapterById/{id}")
    public Result updateById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id,
            @ApiParam(name = "chapter", value = "章节对象", required = true)
            @RequestBody EduChapter chapter) {
        chapter.setId(id);
        eduChapterService.updateById(chapter);
        return Result.ok();
    }

    /**
     * 根据ID删除章节
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("/removeChapterById/{id}")
    public Result removeById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id) {
        boolean result = eduChapterService.removeChapterById(id);
        if (result) {
            return Result.ok();
        } else {
            return Result.error().message("删除失败");
        }
    }

}

