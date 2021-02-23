package com.dwl.service_edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwl.common_utils.util.BeanUtil;
import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_edu.entity.EduTeacher;
import com.dwl.service_edu.entity.vo.TeacherQuery;
import com.dwl.service_edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2020-11-12
 */

@Api(tags = "讲师管理")
@CrossOrigin // 跨域
@RestController
@RequestMapping("/eduService/eduTeacher")
public class EduTeacherController {

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EduTeacherController.class);

    /**
     * 讲师服务类
     */
    private EduTeacherService eduTeacherService;

    /**
     * 无参构造方法
     */
    public EduTeacherController() {
    }

    /**
     * 带参构造方法
     *
     * @param eduTeacherService 讲师 服务类
     */
    @Autowired
    public EduTeacherController(EduTeacherService eduTeacherService) {
        this.eduTeacherService = eduTeacherService;
    }

    /**
     * 查询讲师表所有数据
     *
     * @return Result
     */
    @ApiOperation(value = "所有讲师列表")
        @GetMapping("/findAll")
    public Result findAllTeacher(@ApiParam(name = "id", value = "讲师id") String id) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(id)) {
            wrapper.eq("id", id);
        }
        List<EduTeacher> list = eduTeacherService.list(wrapper);
        return Result.ok().data("items", list);
    }

    /**
     * 分页查询讲师的方法
     *
     * @param current 当前页
     * @param limit   每页记录数
     * @return Result
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public Result pageListTeacher(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit) {
        // 创建 page 对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        /* 调用方法实现分页
         * 调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面 */
        eduTeacherService.page(pageTeacher, null);
        return Result.ok().data("total", pageTeacher.getTotal()).data("rows", pageTeacher.getRecords());
    }

    /**
     * 获取指定的前几个名师
     *
     * @param limit
     * @return
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("/listTeacher/{limit}")
    public Result listTeacher(
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable String limit) {
        try {
            return Result.ok().data("teacherList", eduTeacherService.getTeacherList(limit));
        } catch (Exception e) {
            LOGGER.error("获取首页名师数据查询异常：{}", e + "");
            return Result.error().message("获取首页名师数据查询异常：" + e);
        }
    }

    /**
     * 条件查询带分页的方法
     *
     * @param current      当前页
     * @param limit        每页记录数
     * @param teacherQuery 查询条件封装类
     * @return Result
     */
    @ApiOperation(value = "条件查询带分页的方法")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public Result pageTeacherCondition(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit,
            @ApiParam(name = "teacherQuery", value = "讲师查询对象") @RequestBody(required = false) TeacherQuery teacherQuery) {
        try {
            // 调用方法实现条件查询分页
            IPage<EduTeacher> pageTeacher = eduTeacherService.pageTeacher(current, limit, teacherQuery);
            return Result.ok().data("total", pageTeacher.getTotal()).data("rows", pageTeacher.getRecords());

        } catch (Exception e) {
            LOGGER.error("条件查询带分页的方法查询异常：{}", e + "");
            return Result.error().message("条件查询带分页的方法查询异常：" + e);
        }
    }

    /**
     * 根据讲师ID查询数据信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据讲师ID查询数据信息")
    @GetMapping("/getById/{id}")
    public Result getById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        if (StringUtil.isNotEmpty(id)) {
            EduTeacher eduTeacher = eduTeacherService.getById(id);
            if (BeanUtil.isNotEmpty(eduTeacher)) {
                return Result.ok().data("teacher", eduTeacher);
            }
        }
        LOGGER.error("根据讲师ID查询数据信息异常，讲师ID：{}", id);
        return Result.error().message("讲师id为空，请检查数据是否正常！");
    }

    /**
     * 逻辑删除讲师的方法
     *
     * @param id 讲师 id
     * @return 成功还是失败
     */
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("/removeById/{id}")
    @CacheEvict(value = "teacherList", allEntries = true)
    public Result removeById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            LOGGER.error("逻辑删除讲师信息异常，讲师ID：{}", id);
            return Result.error();
        }
    }

    /**
     * 添加讲师接口的方法
     *
     * @param eduTeacher 讲师实体类
     * @return Result
     */
    @ApiOperation(value = "添加讲师接口的方法")
    @PostMapping("/addTeacher")
    @CacheEvict(value = "teacherList", allEntries = true)
    public Result addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return Result.ok();
        } else {
            LOGGER.error("添加讲师信息异常，讲师ID：{}：", eduTeacher.getId());
            return Result.error();
        }
    }

    /**
     * 讲师修改功能
     *
     * @param eduTeacher 讲师实体类
     * @return Result
     */
    @PostMapping("/updateTeacher")
    @CacheEvict(value = "teacherList", allEntries = true)
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return Result.ok();
        } else {
            LOGGER.error("讲师修改信息异常，讲师ID：{}：", eduTeacher.getId());
            return Result.error();
        }
    }
}

