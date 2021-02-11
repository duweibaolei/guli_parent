package com.dwl.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.util.BeanUtil;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.entity.EduVideo;
import com.dwl.service_edu.entity.VideoInfoForm;
import com.dwl.service_edu.mapper.EduVideoMapper;
import com.dwl.service_edu.service.EduVideoService;
import com.dwl.service_edu.client.VodClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EduVideoServiceImpl.class);

    private VodClient vodClient;

    public EduVideoServiceImpl() {
    }

    @Autowired
    public EduVideoServiceImpl(VodClient vodClient) {
        this.vodClient = vodClient;
    }

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
    @Transactional
    public void removeVideoById(String id) {
        EduVideo video = baseMapper.selectById(id);
        if (BeanUtil.isEmpty(video)) {
            throw new GuLiException(ResultCode.DELETED_ERROR.getStatus(), "没有查询到课时信息！");
        }
        //删除视频资源
        if (StringUtil.isNotEmpty(video.getVideoSourceId())) {
            try {
                vodClient.removeVideo(video.getVideoSourceId());
            } catch (Exception e) {
                throw new GuLiException(ResultCode.DELETED_ERROR.getStatus(), "删除视频异常：" + e);
            }
        }
        Integer result = baseMapper.deleteById(id);
        if (result <= 0) {
            throw new GuLiException(ResultCode.DELETED_ERROR.getStatus(), "删除视频课时异常！");
        }
    }

    /**
     * 根据courseId删除课时业务方法
     *
     * @param courseId
     */
    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }

}
