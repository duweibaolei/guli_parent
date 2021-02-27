package com.dwl.service_statistics.controller;


import com.dwl.common_utils.Result.Result;
import com.dwl.service_statistics.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-02-26
 */
@RestController
@RequestMapping("/serviceStatistics/statisticsDaily")
@CrossOrigin
@Api(tags = "网站统计日数据")
public class StatisticsDailyController {

    private StatisticsDailyService staService;

    @Autowired
    public StatisticsDailyController(StatisticsDailyService staService) {
        this.staService = staService;
    }

    public StatisticsDailyController() {
    }

    @ApiOperation(value = "统计某一天注册人数,生成统计数据")
    @PostMapping("registerCount/{day}")
    public Result registerCount(
            @ApiParam(name = "day", value = "天", required = true) @PathVariable String day) {
        staService.registerCount(day);
        return Result.ok();
    }

    @ApiOperation(value = "图表显示，返回两部分数据，日期json数组，数量json数组")
    @GetMapping("showData/{type}/{begin}/{end}")
    public Result showData(
            @ApiParam(name = "type", value = "类型", required = true) @PathVariable String type,
            @ApiParam(name = "begin", value = "开始") @PathVariable String begin,
            @ApiParam(name = "end", value = "结束") @PathVariable String end) {
        Map<String, Object> map = staService.getShowData(type, begin, end);
        return Result.ok().data(map);
    }
}

