package com.dwl.service_vod.vodUtil;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.vod.upload.impl.VoDProgressListener;
import com.dwl.service_base.util.RedisUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 上传进度回调方法类
 * 当您开启上传进度回调时该事件回调才会生效。
 * OSS分片上传成功或失败均触发相应的回调事件，您可根据业务逻辑处理相应的事件回调。
 * 当创建音视频信息成功后，此上传进度回调中的videoId为本次上传生成的视频ID，您可以根据视频ID进行音视频管理。
 * 当创建图片信息成功后，此上传进度回调中的ImageId为本次上传生成的图片ID，您可以根据视频ID进行图片管理。
 */
@Data
public class PutObjectProgressListener implements VoDProgressListener {

    /**
     * 日志服务
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(PutObjectProgressListener.class);

    /**
     * 已成功上传至OSS的字节数
     */
    private long bytesWritten = 0;
    /**
     * 原始文件的总字节数
     */
    private long totalBytes = -1;
    /**
     * 本次上传成功标记
     */
    private boolean succeed = false;
    /**
     * 视频ID
     */
    private String videoId;
    /**
     * 图片ID
     */
    private String imageId;

    @Override
    public void onVidReady(String s) {
    }

    private RedisUtils redisUtils;

    public PutObjectProgressListener(){}

    public PutObjectProgressListener(RedisUtils redisUtils){
        this.redisUtils = redisUtils;
    }

    @Override
    public void onImageIdReady(String s) {
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            //开始上传事件
            case TRANSFER_STARTED_EVENT:
                if (videoId != null) {
                    LOGGER.info("Start to upload videoId：{}", videoId);
                }
                if (imageId != null) {
                    LOGGER.info("Start to upload imageId：{}", imageId);
                }
                break;
            //计算待上传文件总大小事件通知，只有调用本地文件方式上传时支持该事件
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                LOGGER.error("{}：bytes in total will be uploaded to OSS.", this.totalBytes);
                break;
            //已经上传成功文件大小事件通知
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten = bytes;
                if (this.bytesWritten > 0) {
                    int percent = (int) (this.bytesWritten / 100.0);
                    System.out.println("------------------------" + percent);
                    redisUtils.set("progressData", percent, 10L, TimeUnit.MINUTES);
                }
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                    LOGGER.error("{}: bytes have been written at this time, upload progress: {}  %( {} / {} ）", bytes, percent, this.bytesWritten, this.totalBytes);
                } else {
                    LOGGER.error("{}: bytes have been written at this time, upload sub total : ( {} )", bytes, this.bytesWritten);
                }
                break;
            //文件全部上传成功事件通知
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                if (videoId != null) {
                    LOGGER.error("Succeed to upload videoId：{}, {} bytes have been transferred in total.", videoId, this.bytesWritten);
                }
                if (imageId != null) {
                    LOGGER.error("Succeed to upload imageId：{}, {} bytes have been transferred in total.", videoId, this.bytesWritten);
                }
                break;
            //文件上传失败事件通知
            case TRANSFER_FAILED_EVENT:
                if (videoId != null) {
                    LOGGER.error("Failed to upload videoId：{}, {} bytes have been transferred", videoId, this.bytesWritten);
                }
                if (imageId != null) {
                    LOGGER.error("Failed to upload imageId：{}, {} bytes have been transferred", videoId, this.bytesWritten);
                }
                break;
            default:
                break;
        }
    }

}
