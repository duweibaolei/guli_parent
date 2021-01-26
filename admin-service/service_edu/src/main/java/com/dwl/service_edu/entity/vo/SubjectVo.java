package com.dwl.service_edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于获取课程分类的第一层信息的数据
 */
@Data
public class SubjectVo implements Serializable {

    private static final long serialVersionUID = 5425831067194951093L;
    /**
     * id
     */
    private String id;

    /**
     * 标题名称
     */
    private String title;
}
