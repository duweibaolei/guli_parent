package com.dwl.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.util.BeanUtil;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.entity.EduChapter;
import com.dwl.service_edu.entity.EduVideo;
import com.dwl.service_edu.entity.vo.ChapterNestedVo;
import com.dwl.service_edu.entity.vo.ChapterVo;
import com.dwl.service_edu.mapper.EduChapterMapper;
import com.dwl.service_edu.service.EduChapterService;
import com.dwl.service_edu.service.EduVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2021-01-18
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    /**
     * 日志信息
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(EduChapterServiceImpl.class);

    private EduVideoService videoService;

    public EduChapterServiceImpl() {
    }

    @Autowired
    public EduChapterServiceImpl(EduVideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * 获取课程下的所有章节和课时信息（树形）
     *
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterNestedVo> nestedChapterList(String courseId) {
        List<ChapterNestedVo> nestedVoList = new ArrayList<>();
        // 获取课程章节
        QueryWrapper<EduChapter> queryWrapperOne = new QueryWrapper<>();
        queryWrapperOne.eq("course_id", courseId);
        queryWrapperOne.orderByAsc("sort");
        List<EduChapter> chapterList = this.list(queryWrapperOne);
        if (BeanUtil.isEmpty(chapterList)) {
            LOGGER.error("获取全部课程章节的信息（树形）数据为空！");
        }
        // 查询课程章节下的课时
        QueryWrapper<EduVideo> queryWrapperTwo = new QueryWrapper<>();
        queryWrapperTwo.eq("course_id", courseId);
        queryWrapperOne.orderByAsc("sort");
        List<EduVideo> videoList = videoService.list(queryWrapperTwo);
        if (BeanUtil.isEmpty(videoList)) {
            LOGGER.error("获取全部课程章节的课时信息（树形）为空！");
        }
        // 开始拼装数据
        for (EduChapter chapter : chapterList) {
            // 拼装第一层的数据
            ChapterNestedVo chapterNestedVo = new ChapterNestedVo();
            BeanUtils.copyProperties(chapter, chapterNestedVo);
            // 拼装第二层的数据
            List<ChapterVo> chapterVoList = new ArrayList<>();
            for (EduVideo video : videoList) {
                if (chapter.getId().equals(video.getChapterId())) {
                    ChapterVo chapterVo = new ChapterVo();
                    BeanUtils.copyProperties(video, chapterVo);
                    chapterVoList.add(chapterVo);
                }
            }
            chapterNestedVo.setChildren(chapterVoList);
            nestedVoList.add(chapterNestedVo);
        }
        return nestedVoList;
    }

    /**
     * 根据ID删除章节
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeChapterById(String id) {
        //根据id查询是否存在视频，如果有则提示用户尚有子节点
        if (videoService.getCountByChapterId(id)) {
            throw new GuLiException(ResultCode.UPDATE_ERROR.getStatus(), "该分章节下存在视频课程，请先删除视频课程");
        }
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }

    /**
     * 根据courseId删除章节业务方法
     *
     * @param courseId
     */
    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }
}
