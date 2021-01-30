package com.dwl.service_edu.controller;


import com.dwl.common_utils.Result.Result;
import com.dwl.service_edu.entity.VideoInfoForm;
import com.dwl.service_edu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-01-15
 */
@RestController
@RequestMapping("/eduService/eduVideo")
@Api("课时管理")
@CrossOrigin //跨域
public class EduVideoController {

    public static final Logger LOGGER = LoggerFactory.getLogger(EduVideoController.class);

    /**
     * 课程课时信息服务类
     */
    private EduVideoService eduVideoService;

    public EduVideoController() {
    }

    @Autowired
    public EduVideoController(EduVideoService eduVideoService) {
        this.eduVideoService = eduVideoService;
    }

    /**
     * 新增课时
     *
     * @param videoInfoForm
     * @return Result
     */
    @ApiOperation(value = "新增课时")
    @PostMapping("/saveVideoInfo")
    public Result save(
            @ApiParam(name = "videoForm", value = "课时对象", required = true)
            @RequestBody VideoInfoForm videoInfoForm) {
        try {
            eduVideoService.saveVideoInfo(videoInfoForm);
            return Result.ok();
        } catch (Exception e) {
            return Result.error().message("新增课时异常：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询课时
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID查询课时")
    @GetMapping("videoInfo/{id}")
    public Result getVideInfoById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id) {
        try {
            VideoInfoForm videoInfoForm = eduVideoService.getVideoInfoFormById(id);
            return Result.ok().data("item", videoInfoForm);
        } catch (Exception e) {
            return Result.error().message("查询课时异常：" + e.getMessage());
        }
    }

    /**
     * 更新课时
     *
     * @param videoInfoForm
     * @param id
     * @return
     */
    @ApiOperation(value = "更新课时")
    @PutMapping("updateVideoInfo/{id}")
    public Result updateCourseInfoById(
            @ApiParam(name = "VideoInfoForm", value = "课时基本信息", required = true)
            @RequestBody VideoInfoForm videoInfoForm,
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id) {
        try {
            eduVideoService.updateVideoInfoById(videoInfoForm);
            return Result.ok();
        } catch (Exception e) {
            return Result.error().message("更新课时异常：" + e.getMessage());
        }
    }

    /**
     * 根据ID删除课时
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID删除课时")
    @DeleteMapping("/removeVideoById/{id}")
    public Result removeById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id) {
        try {
            eduVideoService.removeVideoById(id);
            return Result.ok();
        } catch (Exception e) {
            return Result.error().message("删除课时失败：" + e);
        }
    }
}

