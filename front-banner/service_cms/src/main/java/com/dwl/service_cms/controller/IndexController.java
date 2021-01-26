package com.dwl.service_cms.controller;

import com.dwl.common_utils.Result;
import com.dwl.service_cms.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/index")
@CrossOrigin
public class IndexController {
    /**
     * 日志服务
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CrmBannerController.class);

    /**
     * 课程服务类
     */
    private IndexService indexService;

    @Autowired
    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/index")
    public Result index() {
        return Result.ok().data(indexService.getCourseIndex()).data(indexService.getTeacherIndex());
    }


}
