package com.dwl.service_vod.service.impl;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteMezzaninesRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_base.util.RedisUtils;
import com.dwl.service_vod.entity.from.UploadFileFrom;
import com.dwl.service_vod.service.VideoService;
import com.dwl.service_vod.vodUtil.AliyunVodSDKUtils;
import com.dwl.service_vod.vodUtil.ConstantPropertiesUtil;
import com.dwl.service_vod.vodUtil.PutObjectProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    /**
     * 日志信息
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(VideoServiceImpl.class);

    /**
     * redis服务
     */
    private final RedisUtils redisUtils;

    @Autowired
    public VideoServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 视频文件上传至阿里云服务器
     *
     * @param file 上传的文件
     * @return 上传好的视频id
     */
    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            // 获取视频文件的io流
            InputStream inputStream = file.getInputStream();
            // 原来的文件名称带扩展名
            String filename = file.getOriginalFilename();
            // 截取文件名称
            if (StringUtil.isEmpty(filename)) {
                throw new GuLiException(ResultCode.FILE_UPLOAD_ERROR.getStatus(), "上传的视频文件没有名称！");
            }
            String title = filename.substring(0, filename.lastIndexOf("."));

            // 拼装阿里云配置信息
            UploadFileFrom uploadFileFrom = new UploadFileFrom();
            uploadFileFrom.setAccessKeyId(ConstantPropertiesUtil.KEY_ID);
            uploadFileFrom.setAccessKeySecret(ConstantPropertiesUtil.KEY_SECRET);
            uploadFileFrom.setTitle(title);
            uploadFileFrom.setFileName(filename);

            // 创建阿里云上传初始化
            UploadStreamRequest request = new UploadStreamRequest(
                    uploadFileFrom.getAccessKeyId(), uploadFileFrom.getAccessKeySecret(),
                    uploadFileFrom.getTitle(), uploadFileFrom.getFileName(), inputStream);
            /* 开启默认上传进度回调 */
            request.setPrintProgress(true);

            /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
            PutObjectProgressListener listener = new PutObjectProgressListener(redisUtils);
            ProgressEvent progressEvent = new ProgressEvent(ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT);
            listener.progressChanged(progressEvent);
            request.setProgressListener(listener);
            // 获取上传信息
            UploadStreamResponse response = new UploadVideoImpl().uploadStream(request);
            if (response.isSuccess()) {
                return response.getVideoId();
            } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                LOGGER.error("阿里云上传错误，上传id为：{}，错误编码为：{}，上传错误信息为：{}",
                        response.getRequestId(), response.getCode(), response.getMessage());
                throw new GuLiException(ResultCode.FILE_UPLOAD_ERROR.getStatus(), response.getMessage());
            }
        } catch (IOException e) {
            LOGGER.error("上传视频异常：{}", e + "");
            throw new GuLiException(ResultCode.FILE_UPLOAD_ERROR.getStatus(), e + "");
        }
    }

    /**
     * 云端视频删除
     *
     * @param videoId 视频id
     */
    @Override
    public void removeVideo(String videoId) {

        try {
//          // 发送请求客户端
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.KEY_ID, ConstantPropertiesUtil.KEY_SECRET);
//          // 删除视频响应数据
            DeleteVideoRequest request = new DeleteVideoRequest();
//          // 支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            client.getAcsResponse(request);
        } catch (Exception e) {
            LOGGER.error("请求删除异常,对应的视频id[{}] 为：", videoId);
            throw new GuLiException(ResultCode.DELETED_ERROR.getStatus(), "请求删除异常：" + e);
        }
    }

    /**
     * 删除全部的课时相关的视频
     *
     * @param videoIdList 视频id集合
     */
    @Override
    public void removeVideoList(List<String> videoIdList) {
        try {
            // 点播服务初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.KEY_ID, ConstantPropertiesUtil.KEY_SECRET);
            DeleteMezzaninesRequest request = new DeleteMezzaninesRequest();
            //支持传入多个视频ID，多个用逗号分隔
            String videoIdStr = StringUtil.join(videoIdList, ",");
            request.setVideoIds(videoIdStr);
            request.setForce(false);
            client.getAcsResponse(request);
        } catch (Exception e) {
            LOGGER.error("请求删除异常,对应的视频id[{}] 为：", StringUtil.join(videoIdList, ","));
            throw new GuLiException(ResultCode.DELETED_ERROR.getStatus(), "请求删除异常：" + e);
        }

    }
}
