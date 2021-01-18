package com.dwl.service_edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于获取课程分类的第一层信息的数据
 */
@Data
public class SubjectNestedVo implements Serializable {

    private static final long serialVersionUID = -5480280389900157305L;

    /**
     * id
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 第一层信息下的第二层信息
     */
    private List<SubjectVo> children = new ArrayList<>();
}
