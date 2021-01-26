package com.dwl.service_edu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程章节的第二层信息实体类
 */
@Data
public class ChapterVo implements Serializable {

    private static final long serialVersionUID = 5898508231209401219L;

    /**
     * id
     */
    private String id;

    /**
     * 标题名称
     */
    private String title;

    /**
     * 是否免费
     */
    private Integer isFree;


    @ApiModelProperty(value = "云服务器上存储的视频文件名称")
    private String videoOriginalName;

}
