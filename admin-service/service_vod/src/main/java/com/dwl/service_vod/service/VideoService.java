package com.dwl.service_vod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 视频相关的服务类
 */
public interface VideoService {

    /**
     * 视频文件上传至阿里云服务器
     *
     * @param file 上传的文件
     * @return 上传好的视频id
     */
    String uploadVideo(MultipartFile file) throws IOException;

    /**
     * 云端视频删除
     *
     * @param videoId 视频id
     */
    void removeVideo(String videoId) throws ClientException;

    /**
     * 删除全部的课时相关的视频
     *
     * @param videoIdList 视频id集合
     */
    void removeVideoList(List<String> videoIdList);

}
