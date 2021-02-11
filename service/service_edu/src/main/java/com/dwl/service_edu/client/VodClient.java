package com.dwl.service_edu.client;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_edu.client.impl.VodFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 调用视频相关的服务
 */
@Component
/* 用于指定从哪个服务中调用功能 ，名称与被调用的服务名保持一致。采用熔断机制。*/
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class)
public interface VodClient {

    @DeleteMapping(value = "/vod/video/removeVideo/{videoId}")
    Result removeVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping(value = "/vod/video/removeVideoList")
    Result removeVideoList(@RequestParam("videoIdList") List<String> videoIdList);
}
