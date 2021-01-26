package com.dwl.service_cms.controller;

import com.dwl.service_cms.service.CrmBannerService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/bannerApi")
@Api("网站首页Banner列表")
@CrossOrigin //跨域
public class BannerApiController {

    /**
     * 日志服务
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CrmBannerController.class);

    /**
     * 首页横幅广告服务类
     */
    private CrmBannerService bannerService;

    @Autowired
    public BannerApiController(CrmBannerService bannerService) {
        this.bannerService = bannerService;
    }


}
