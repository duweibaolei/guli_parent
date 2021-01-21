package com.dwl.service_vod.controller;


import com.dwl.common_utils.Result;
import com.dwl.service_vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api("阿里云视频点播微服务")
@CrossOrigin //跨域
@RestController
@RequestMapping("vod/video")
public class VideoAdminController {

    /**
     * 日志信息
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(VideoAdminController.class);

    /**
     * 视频文件上传服务类
     */
    private VideoService videoService;

    @Autowired
    public VideoAdminController(VideoService videoService) {
        this.videoService = videoService;
    }

    public VideoAdminController() {
    }


    /**
     * 视频文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation("视频文件上传")
    @PostMapping("/upload")
    public Result uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {

        try {
            String videoId = videoService.uploadVideo(file);
            return Result.ok().message("视频上传成功").data("videoId", videoId);
        } catch (Exception e) {
            return Result.error().message("视频文件上传异常：" + e);
        }
    }

    /**
     * 云端视频删除
     *
     * @param videoId
     * @return
     */
    @ApiOperation("云端视频删除")
    @DeleteMapping("/removeVideo/{videoId}")
    public Result removeVideo(
            @ApiParam(name = "videoId", value = "云端视频id", required = true)
            @PathVariable String videoId) {
        try {
            videoService.removeVideo(videoId);
            return Result.ok().message("视频删除成功");
        } catch (Exception e) {
            return Result.error().message("视频删除异常：" + e);
        }

    }
}
