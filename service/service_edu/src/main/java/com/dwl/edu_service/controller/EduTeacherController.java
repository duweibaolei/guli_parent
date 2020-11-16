package com.dwl.edu_service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwl.common_utils.R;
import com.dwl.edu_service.entity.EduTeacher;
import com.dwl.edu_service.entity.vo.TeacherQuery;
import com.dwl.edu_service.service.EduTeacherService;
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
     * @param eduTeacherService
     */
    @Autowired
    public EduTeacherController(EduTeacherService eduTeacherService) {
        this.eduTeacherService = eduTeacherService;
    }

    /**
     * 查询讲师表所有数据
     *
     * @return
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items", list);
    }

    /**
     * 分页查询讲师的方法
     *
     * @param current 当前页
     * @param limit   每页记录数
     * @return
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(
            @ApiParam(name = "page", value = "当前页", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit) {
        // 创建 page 对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

//        try {
//            int i = 10/0;
//        }catch(Exception e) {
//            //执行自定义异常
//            throw new GuliException(20001,"执行了自定义异常处理....");
//        }

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        eduTeacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合

        return R.ok().data("total", total).data("rows", records);

    }

    /**
     * 条件查询带分页的方法
     *
     * @param current      当前页
     * @param limit        每页记录数
     * @param teacherQuery 查询条件封装类
     * @return
     */
    @ApiOperation(value = "条件查询带分页的方法")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "page", value = "当前页", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit,
            @ApiParam(name = "teacherQuery", value = "查询条件", required = false) @RequestBody(required = false) TeacherQuery teacherQuery) {

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
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 逻辑删除讲师的方法
     *
     * @param id
     * @return 成功还是失败
     */
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("/removeById/{id}")
    public R removeById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 添加讲师接口的方法
     *
     * @param eduTeacher
     * @return
     */
    @ApiOperation(value = "添加讲师接口的方法")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 根据讲师id进行查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据讲师id进行查询")
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    /**
     * 讲师修改功能
     *
     * @param eduTeacher
     * @return
     */
    @ApiOperation(value = "讲师修改功能")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}

