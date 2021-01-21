package com.dwl.service_vod.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.dwl.common_utils.ResultCode;
import com.dwl.common_utils.StringUtil;
import com.dwl.service_base.config.RedisConfig;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_vod.entity.from.UploadFileFrom;
import com.dwl.service_vod.service.VideoService;
import com.dwl.service_vod.vodUtil.AliyunVodSDKUtils;
import com.dwl.service_vod.vodUtil.ConstantPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class VideoServiceImpl implements VideoService {

    /**
     * 日志信息
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(VideoServiceImpl.class);

    /**
     * 创建OSSClient全局变量。
     */
    private static OSS ossClient = null;

    private RedisConfig redisConfig;

    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            // 获取视频文件的io流
            InputStream inputStream = file.getInputStream();
            // 原来的文件名称带扩展名
            String filename = file.getOriginalFilename();
            // 截取文件名称
            String title = filename.substring(0, filename.lastIndexOf("."));

            UploadFileFrom uploadFileFrom = new UploadFileFrom();
            uploadFileFrom.setAccessKeyId(ConstantPropertiesUtil.KEY_ID);
            uploadFileFrom.setAccessKeySecret(ConstantPropertiesUtil.KEY_SECRET);
            uploadFileFrom.setTitle(title);
            uploadFileFrom.setFileName(filename);

            return AliyunVodSDKUtils.uploadStream(uploadFileFrom, inputStream);
        } catch (IOException e) {
            LOGGER.error("上传视频异常：{}", e + "");
            throw new GuLiException(ResultCode.FILE_UPLOAD_ERROR.getStatus(), e + "");
        }
    }

    /**
     * 云端删除视频
     *
     * @param videoId
     */
    @Override
    public void removeVideo(String videoId) {

        try {
            // 发送请求客户端
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.KEY_ID, ConstantPropertiesUtil.KEY_SECRET);
            // 删除视频响应数据
            DeleteVideoResponse response = new DeleteVideoResponse();
            // 删除视频请求数据
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            response = client.getAcsResponse(request);
            // 如果没有返回请求id，这说明没有返回值进行报错
            if (StringUtil.isNotEmpty(response.getRequestId())) {
                LOGGER.error("请求删除没有返回值,对应的视频id[{}] 为：", videoId);
                throw new GuLiException(ResultCode.DELETED_ERROR.getStatus(), "请求删除没有返回值，请检查视频是否删除成功！");
            }
        } catch (Exception e) {
            LOGGER.error("请求删除异常,对应的视频id[{}] 为：", videoId);
            throw new GuLiException(ResultCode.DELETED_ERROR.getStatus(), "请求删除异常：" + e);
        }
    }

    /**
     * 上初始化大文件上传环境，返回uploadId
     *
     * @param objectName
     * @return
     */
    @Override
    public String initUpload(String objectName) {
        ossClient = new OSSClientBuilder().build(ConstantPropertiesUtil.END_POINT, ConstantPropertiesUtil.KEY_ID, ConstantPropertiesUtil.KEY_SECRET);
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(ConstantPropertiesUtil.BUCKET_NAME, objectName);

        // 如果需要在初始化分片时设置文件存储类型，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // request.setObjectMetadata(metadata);

        // 初始化分片。
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个uploadId发起相关的操作，如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();
        //将uploadId缓存到redis,2个小时有效
        redisConfig.setCacheObject("uploadId:" + uploadId, objectName, 120 * 60 * 1000, TimeUnit.MINUTES);
        return uploadId;

    }

    @Override
    public String uploadChunk(String uploadId, Integer chunkId, MultipartFile chunkFile) {
        return null;
    }

    @Override
    public String completeFile(String uploadId) {
        return null;
    }
}
