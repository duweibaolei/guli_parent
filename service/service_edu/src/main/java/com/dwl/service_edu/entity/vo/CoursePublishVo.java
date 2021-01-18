package com.dwl.service_edu.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel("课程发布信息")
@Data
public class CoursePublishVo implements Serializable {

    private static final long serialVersionUID = 2311783320548224973L;

    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}
