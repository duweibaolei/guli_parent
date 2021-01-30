package com.dwl.service_edu.client.impl;

import com.dwl.common_utils.Result.Result;
import com.dwl.service_edu.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {

    @Override
    public Result removeVideo(String videoId) {
        return Result.error().message("删除视频出错了");
    }

    @Override
    public Result removeVideoList(List<String> videoIdList) {
        return Result.error().message("删除多个视频出错了");
    }
}
