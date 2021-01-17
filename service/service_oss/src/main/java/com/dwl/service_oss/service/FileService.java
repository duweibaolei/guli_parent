package com.dwl.service_oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 文件上传至阿里云
     * @param file
     * @param host
     * @return
     */
    String upload(MultipartFile file, String host);

}
