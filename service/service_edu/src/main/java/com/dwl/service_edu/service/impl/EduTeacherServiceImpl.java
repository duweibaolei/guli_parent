package com.dwl.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_edu.entity.EduTeacher;
import com.dwl.service_edu.entity.vo.TeacherQuery;
import com.dwl.service_edu.mapper.EduTeacherMapper;
import com.dwl.service_edu.service.EduTeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2020-11-12
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    /**
     * 日志服务
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(EduTeacherServiceImpl.class);

    /**
     * 获取指定的前几条讲师数据
     *
     * @param limit 每页记录数
     * @return
     */
    @Override
    public List<EduTeacher> getTeacherList(String limit) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit " + limit);
        queryWrapper.orderByDesc("sort");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 讲师条件查询带分页的方法
     *
     * @param current      当前页
     * @param limit        每页记录数
     * @param teacherQuery 查询条件封装类
     * @return IPage<EduTeacher>
     */
    @Override
    public IPage<EduTeacher> pageTeacher(long current, long limit, TeacherQuery teacherQuery) {
        // 构建查询条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        // 多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        // 判断条件值是否为空，如果不为空拼接条件
        if (StringUtil.isNotEmpty(name)) {
            queryWrapper.like("name", name); // 构建条件
        }
        if (level != null && level > 0) {
            queryWrapper.eq("level", level);
        }
        if (StringUtil.isNotEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }
        if (StringUtil.isNotEmpty(end)) {
            queryWrapper.le("gmt_create", end);
        }
        return baseMapper.selectPage(new Page<>(current, limit), queryWrapper);
    }
}
