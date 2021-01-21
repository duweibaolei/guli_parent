package com.dwl.service_vod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {

    /**
     * 视频文件上传
     *
     * @param file
     * @return
     */
    String uploadVideo(MultipartFile file) throws IOException;

    /**
     * 云端视频删除
     *
     * @param videoId
     */
    void removeVideo(String videoId) throws ClientException;

    /**
     * 大文件上传初始化
     *
     * @param objectName
     * @return
     */
    String initUpload(String objectName);

    /**
     * 上传大文件的Chunk
     *
     * @param uploadId
     * @param chunkId
     * @param chunkFile
     * @return
     */
    String uploadChunk(String uploadId, Integer chunkId, MultipartFile chunkFile);

    /**
     * 大文件上传完成后合并
     *
     * @param uploadId
     * @return
     */
    String completeFile(String uploadId);
}
