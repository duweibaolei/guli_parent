package com.dwl.service_edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.EduVideo;
import com.dwl.service_edu.entity.VideoInfoForm;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-01-15
 */
public interface EduVideoService extends IService<EduVideo> {

    /**
     * 根据课程id删除课时信息
     *
     * @param chapterId
     * @return
     */
    boolean getCountByChapterId(String chapterId);

    /**
     * 新增课时
     *
     * @param videoInfoForm
     */
    void saveVideoInfo(VideoInfoForm videoInfoForm);

    /**
     * 根据ID查询课时
     *
     * @param id
     * @return
     */
    VideoInfoForm getVideoInfoFormById(String id);

    /**
     * 更新课时
     *
     * @param videoInfoForm
     */
    void updateVideoInfoById(VideoInfoForm videoInfoForm);

    /**
     * 根据ID删除课时
     *
     * @param id
     * @return
     */
    boolean removeVideoById(String id);
}
