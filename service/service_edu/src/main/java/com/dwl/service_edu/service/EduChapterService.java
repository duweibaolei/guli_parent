package com.dwl.service_edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.EduChapter;
import com.dwl.service_edu.entity.vo.ChapterNestedVo;
import com.dwl.service_edu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-01-18
 */
public interface EduChapterService extends IService<EduChapter> {

    /**
     * 获取课程下的所有章节和课时信息（树形）
     *
     * @param courseId
     * @return
     */
    List<ChapterNestedVo> nestedChapterList(String courseId);

    /**
     * 根据ID删除章节
     *
     * @param id
     * @return
     */
    boolean removeChapterById(String id);

    /**
     * 根据courseId删除章节业务方法
     *
     * @param courseId
     */
    void removeByCourseId(String courseId);

}
