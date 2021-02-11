package com.dwl.service_vod.vodUtil;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_vod.entity.from.UploadFileFrom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 阿里云视频点播工具类
 */
public class AliyunVodSDKUtils {

    /**
     * 日志信息
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(AliyunVodSDKUtils.class);

    /**
     * 点播服务接入区域
     */
    private static final String REGION_ID = "cn-shanghai";

    /**
     * 点播服务初始化
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     * @throws ClientException
     */
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(REGION_ID, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 流式上传接口
     *
     * @param uploadFileFrom
     * @param inputStream
     */
    public static String uploadStream(UploadFileFrom uploadFileFrom,
                                      InputStream inputStream) {
        UploadStreamRequest request = new UploadStreamRequest(
                uploadFileFrom.getAccessKeyId(), uploadFileFrom.getAccessKeySecret(),
                uploadFileFrom.getTitle(), uploadFileFrom.getFileName(), inputStream);

        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        // request.setShowWaterMark(true);

        /* 自定义消息回调设置，参数说明参考文档 https://help.aliyun.com/document_detail/86952.html#UserData */
        //request.setUserData(""{\"Extend\":{\"test\":\"www\",\"localId\":\"xxxx\"},\"MessageCallback\":{\"CallbackURL\":\"http://test.test.com\"}}"");

        /* 视频分类ID(可选) */
        //request.setCateId(0);

        /* 视频标签,多个用逗号分隔(可选) */
        if (StringUtil.isNotEmpty(uploadFileFrom.getTags())) {
            request.setTags(uploadFileFrom.getTags());
        }

        /* 视频描述(可选) */
        if (StringUtil.isNotEmpty(uploadFileFrom.getDescription())) {
            request.setDescription(uploadFileFrom.getDescription());
        }

        /* 封面图片(可选) */
        if (StringUtil.isNotEmpty(uploadFileFrom.getCoverURL())) {
            request.setCoverURL(uploadFileFrom.getCoverURL());
        }

        /* 模板组ID(可选) */
        if (StringUtil.isNotEmpty(uploadFileFrom.getTemplateGroupId())) {
            request.setTemplateGroupId(uploadFileFrom.getTemplateGroupId());
        }

        /* 工作流ID(可选) */
        if (StringUtil.isNotEmpty(uploadFileFrom.getWorkflowId())) {
            request.setWorkflowId(uploadFileFrom.getWorkflowId());
        }

        /* 存储区域(可选) */
        if (StringUtil.isNotEmpty(uploadFileFrom.getStorageLocation())) {
            request.setStorageLocation(uploadFileFrom.getStorageLocation());
        }

        /* 开启默认上传进度回调 */
        request.setPrintProgress(true);

        /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
        PutObjectProgressListener listener = new PutObjectProgressListener();
        ProgressEvent progressEvent = new ProgressEvent(ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT);
        listener.progressChanged(progressEvent);
        request.setProgressListener(listener);

        /* 设置应用ID*/
        if (StringUtil.isNotEmpty(uploadFileFrom.getAppId())) {
            request.setAppId(uploadFileFrom.getAppId());
        }

        /* 点播服务接入点 */
        if (StringUtil.isNotEmpty(uploadFileFrom.getApiRegionId())) {
            request.setApiRegionId(uploadFileFrom.getApiRegionId());
        }

        /* ECS部署区域*/
        if (StringUtil.isNotEmpty(uploadFileFrom.getEcsRegionId())) {
            request.setEcsRegionId(uploadFileFrom.getEcsRegionId());
        }

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        if (response.isSuccess()) {
            return response.getVideoId();
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            LOGGER.error("阿里云上传错误，上传id为：{}，错误编码为：{}，上传错误信息为：{}",
                    response.getRequestId(), response.getCode(), response.getMessage());
            throw new GuLiException(ResultCode.FILE_UPLOAD_ERROR.getStatus(), response.getMessage());
        }
    }

}
