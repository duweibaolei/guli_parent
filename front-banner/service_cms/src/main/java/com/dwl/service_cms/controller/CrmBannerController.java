package com.dwl.service_cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwl.common_utils.Result.Result;
import com.dwl.service_cms.entity.CrmBanner;
import com.dwl.service_cms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-01-25
 */
@RestController
@Api("网站首页广告列表")
@RequestMapping("/cms/crmBanner")
public class CrmBannerController {

    /**
     * 日志服务
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CrmBannerController.class);

    /**
     * 首页横幅广告服务类
     */
    private final CrmBannerService bannerService;

    @Autowired
    public CrmBannerController(CrmBannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * 获取首页横幅广告
     *
     * @return Result
     */
    @ApiOperation(value = "获取横幅广告")
    @GetMapping("/getAllBanner")
    public Result index() {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        List<CrmBanner> list = bannerService.list(wrapper);
        return Result.ok().data("bannerList", list);
    }

    /**
     * 首页横幅广告分页
     *
     * @param current
     * @param limit
     * @return Result
     */
    @ApiOperation("首页横幅广告分页")
    @GetMapping("/pageListBanner/{current}/{limit}")
    public Result pageListBanner(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable long limit) {

        // 创建 page 对象
        Page<CrmBanner> bannerPage = new Page<>(current, limit);

        /* 调用方法实现分页
         * 调用方法时候，底层封装，把分页所有数据封装到bannerPage对象里面 */
        bannerService.page(bannerPage, null);

        long total = bannerPage.getTotal();//总记录数
        List<CrmBanner> records = bannerPage.getRecords(); //数据list集合

        return Result.ok().data("total", total).data("rows", records);
    }

    /**
     * 获取单条横幅广告
     *
     * @param id
     * @return Result
     */
    @ApiOperation("获取单条横幅广告")
    @GetMapping("/getById/{id}")
    public Result getById(
            @ApiParam(name = "id", value = "横幅广告id", required = true)
            @PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return Result.ok().data("banner", banner);
    }

    /**
     * 新增横幅广告
     *
     * @param banner
     * @return Result
     */
    @ApiOperation("新增横幅广告")
    @PostMapping("/saveBanner")
    public Result saveBanner(@RequestBody CrmBanner banner) {
        boolean flag = bannerService.save(banner);
        if (flag) {
            return Result.ok();
        } else {
            return Result.ok().message("新增异常！");
        }
    }

    /**
     * 修改横幅广告
     *
     * @param banner
     * @return Result
     */
    @ApiOperation("修改横幅广告")
    @PostMapping("/updateBanner")
    public Result updateBanner(@RequestBody CrmBanner banner) {
        boolean flag = bannerService.updateById(banner);
        if (flag) {
            return Result.ok();
        } else {
            return Result.ok().message("修改异常！");
        }
    }

    /**
     * 删除横幅广告
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除横幅广告")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {

        boolean flag = bannerService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.ok().message("删除异常！");
        }
    }
}

