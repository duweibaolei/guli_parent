package com.dwl.service_cms.service;

import java.util.List;

public interface IndexService {

    /**
     * 获取头几位讲师
     *
     * @param limit
     * @return
     */
    List<Object> getTeacherIndex(String limit);

    /**
     * 获取头几们课程
     *
     * @param limit
     * @return
     */
    List<Object> getCourseIndex(String limit);

}
