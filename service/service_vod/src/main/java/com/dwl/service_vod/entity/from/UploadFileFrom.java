package com.dwl.service_vod.entity.from;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传实体类
 */
@Data
public class UploadFileFrom {

    /**
     * 阿里云ossAccessKeyId
     */
    @ApiModelProperty(value = "阿里云ossAccessKeyId")
    private String accessKeyId;

    /**
     * 阿里云oss密钥
     */
    @ApiModelProperty(value = "阿里云oss密钥")
    private String accessKeySecret;

    /**
     * 文件标题
     */
    @ApiModelProperty(value = "文件标题")
    private String title;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印
     */
    @ApiModelProperty(value = "是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印")
    private boolean showWaterMark;

    /**
     * 自定义消息回调设置，参数说明参考文档 https://help.aliyun.com/document_detail/86952.html#UserData
     */
    @ApiModelProperty(value = "自定义消息回调设置，参数说明参考文档 https://help.aliyun.com/document_detail/86952.html#UserData")
    private String UserData;

    /**
     * 视频分类ID(可选)
     */
    @ApiModelProperty(value = "视频分类ID(可选)")
    private Long cateId;

    /**
     * 视频标签,多个用逗号分隔(可选)
     */
    @ApiModelProperty(value = "视频标签,多个用逗号分隔(可选)")
    private String tags;


    /**
     * 视频描述(可选)
     */
    @ApiModelProperty(value = "视频描述(可选)")
    private String description;

    /**
     * 封面图片(可选)
     */
    @ApiModelProperty(value = "封面图片(可选)")
    private String coverURL;

    /**
     * 模板组ID(可选)
     */
    @ApiModelProperty(value = "模板组ID(可选)")
    private String templateGroupId;

    /**
     * 工作流ID(可选)
     */
    @ApiModelProperty(value = "工作流ID(可选)")
    private String workflowId;

    /**
     * 存储区域(可选)
     */
    @ApiModelProperty(value = "存储区域(可选)")
    private String storageLocation;


    /**
     * 应用ID(可选)
     */
    @ApiModelProperty(value = "应用ID(可选)")
    private String appId;

    /**
     * 点播服务接入点
     */
    @ApiModelProperty(value = "点播服务接入点")
    private String apiRegionId;

    /**
     * ECS部署区域
     */
    @ApiModelProperty(value = "ECS部署区域")
    private String ecsRegionId;

}
