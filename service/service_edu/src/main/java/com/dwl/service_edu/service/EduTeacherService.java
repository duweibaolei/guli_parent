package com.dwl.service_edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.EduTeacher;
import com.dwl.service_edu.entity.vo.TeacherQuery;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author dwl
 * @since 2020-11-12
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     * 获取指定的前几条讲师数据
     *
     * @param limit
     * @return
     */
    List<EduTeacher> getTeacherList(String limit);

    /**
     * 讲师条件查询带分页的方法
     *
     * @param current      当前页
     * @param limit        每页记录数
     * @param teacherQuery 查询条件封装类
     * @return IPage<EduTeacher>
     */
    IPage<EduTeacher> pageTeacher(long current, long limit, TeacherQuery teacherQuery);
}
