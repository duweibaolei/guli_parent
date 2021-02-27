package com.dwl.service_statistics.schedule;

import com.dwl.common_utils.util.DateUtil;
import com.dwl.service_statistics.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务类
 * springboot默认支持六位，其中年默认是当前年。
 */
@Component
public class ScheduledTask {

    private StatisticsDailyService staService;

    @Autowired
    public ScheduledTask(StatisticsDailyService staService) {
        this.staService = staService;
    }

    public ScheduledTask() {
    }

    /**
     * 0/5 * * * * ?表示每隔5秒执行一次这个方法
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        System.out.println("**************task1执行了..");
    }

    /**
     * 在每天凌晨1点，把前一天数据进行数据查询添加
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        staService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }

}