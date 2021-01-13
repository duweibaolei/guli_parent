package com.dwl.service_edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwl.common_utils.Result;
import com.dwl.common_utils.StringUtil;
import com.dwl.service_edu.entity.EduTeacher;
import com.dwl.service_edu.entity.vo.TeacherQuery;
import com.dwl.service_edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

@Api("讲师管理")
@CrossOrigin // 跨域
@RestController
@RequestMapping("/eduService/eduTeacher")
public class EduTeacherController {

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
    public Result findAllTeacher() {
        List<EduTeacher> list = eduTeacherService.list(null);
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

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合

        return Result.ok().data("total", total).data("rows", records);

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

        // 创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        // 构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        // 多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        // 判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name); // 构建条件
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        // 调用方法实现条件查询分页
        eduTeacherService.page(pageTeacher, wrapper);

        long total = pageTeacher.getTotal();// 总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); // 数据list集合
        return Result.ok().data("total", total).data("rows", records);
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
            if (eduTeacher != null) {
                return Result.ok().data("teacher", eduTeacher);
            }
        }
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
    public Result removeById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
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
    public Result addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    /**
     * 讲师修改功能
     *
     * @param eduTeacher 讲师实体类
     * @return Result
     */
    @ApiOperation(value = "讲师修改功能")
    @PostMapping("/updateTeacher")
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }
}

