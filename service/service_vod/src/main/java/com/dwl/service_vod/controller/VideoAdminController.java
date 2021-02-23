package com.dwl.service_vod.controller;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_base.util.RedisUtils;
import com.dwl.service_vod.service.VideoService;
import com.dwl.service_vod.vodUtil.AliyunVodSDKUtils;
import com.dwl.service_vod.vodUtil.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * redis服务类
     */
    private RedisUtils redisUtils;

    @Autowired
    public VideoAdminController(RedisUtils redisUtils, VideoService videoService) {
        this.redisUtils = redisUtils;
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

    /**
     * 获取上传视频的进度
     *
     * @param request
     * @return
     */
    @PostMapping("/percent")
    @ResponseBody
    public Result getUploadPercent(@ApiParam(name = "request", value = "客户端的请求")
                                           HttpServletRequest request) {
        Integer progressData = (Integer) redisUtils.get("progressData");
        return Result.ok().data("progressData", progressData);
    }

    /**
     * 根据视频id获取视频凭证
     */
    @GetMapping("getPlayAuth/{id}")
    public Result getPlayAuth(
            @ApiParam(name = "id", value = "视频id", required = true)
            @PathVariable String id) {
        try {
            // 创建初始化对象
            DefaultAcsClient client =
                    AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.KEY_ID, ConstantPropertiesUtil.KEY_SECRET);
            // 创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            // 向request设置视频id
            request.setVideoId(id);
            // 调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return Result.ok().data("playAuth", playAuth);
        } catch (Exception e) {
            throw new GuLiException(ResultCode.ERROR.getStatus(), "获取凭证失败");
        }
    }

}
