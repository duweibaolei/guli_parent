package com.dwl.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.BeanUtil;
import com.dwl.common_utils.ResultCode;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.entity.EduVideo;
import com.dwl.service_edu.entity.VideoInfoForm;
import com.dwl.service_edu.mapper.EduVideoMapper;
import com.dwl.service_edu.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2021-01-15
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    /**
     * 根据课程id删除课时信息
     *
     * @param chapterId
     * @return
     */
    @Override
    public boolean getCountByChapterId(String chapterId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapterId);
        Integer count = baseMapper.selectCount(queryWrapper);
        return null != count && count > 0;
    }

    /**
     * 新增课时
     *
     * @param videoInfoForm
     */
    @Override
    public void saveVideoInfo(VideoInfoForm videoInfoForm) {
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        int result = baseMapper.insert(video);
        if (result == 0) {
            throw new GuLiException(ResultCode.SAVE_ERROR.getStatus(), "新增课时信息保存失败");
        }
    }

    /**
     * 根据ID查询课时
     *
     * @param id
     * @return
     */
    @Override
    public VideoInfoForm getVideoInfoFormById(String id) {
        //从video表中取数据
        EduVideo video = this.getById(id);
        if (BeanUtil.isEmpty(video)) {
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "查询课时数据不存在");
        }
        //创建videoInfoForm对象
        VideoInfoForm videoInfoForm = new VideoInfoForm();
        BeanUtils.copyProperties(video, videoInfoForm);
        return videoInfoForm;
    }

    /**
     * 更新课时
     *
     * @param videoInfoForm
     */
    @Override
    public void updateVideoInfoById(VideoInfoForm videoInfoForm) {
        // 保存课时基本信息
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        boolean result = this.updateById(video);
        if (!result) {
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "课时信息保存失败");
        }
    }

    /**
     * 根据ID删除课时
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeVideoById(String id) {
        //删除视频资源 TODO
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }
}
