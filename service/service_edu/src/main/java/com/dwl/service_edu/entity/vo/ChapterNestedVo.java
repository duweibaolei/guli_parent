package com.dwl.service_edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于获取章节的第一层信息
 */
@Data
public class ChapterNestedVo implements Serializable {

    private static final long serialVersionUID = 5839102909391485601L;

    /**
     * id
     */
    private String id;

    /**
     * 标题名称
     */
    private String title;

    /**
     * 章节的第二层信息
     */
    private List<ChapterVo> children = new ArrayList<>();
}
