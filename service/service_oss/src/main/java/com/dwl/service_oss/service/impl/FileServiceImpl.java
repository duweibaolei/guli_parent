package com.dwl.service_oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.dwl.common_utils.ResultCode;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_oss.OssUtil.ConstantPropertiesUtil;
import com.dwl.service_oss.service.FileService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);


    @Override
    public String upload(MultipartFile file) {

        // 定义链接oss的链接属性
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String keyId = ConstantPropertiesUtil.KEY_ID;
        String keySecret = ConstantPropertiesUtil.KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String uploadUrl;

        // 按照日期进行分组
        String dateTime = new DateTime().toString("yyyy-MM-dd");
        String fileName = "head_portrait" + "/" + dateTime + "/" + UUID.randomUUID().toString();

        // 通过账户来创建oss
        OSS client = new OSSClientBuilder().build(endPoint, keyId, keySecret);

        // 判断oss实例是否存在，不存在则进行创建
        if (!client.doesBucketExist(bucketName)) {
            // 创建实例
            client.createBucket(bucketName);
            // 设置oss实例的访问权限：公共读
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
        try {
            // 获取文件上传流
            InputStream inputStream = file.getInputStream();

            // 原来的文件名称
            String original = file.getOriginalFilename();
            // 获取文件类型
            String fileType = original.substring(original.lastIndexOf("."));
            // 新的文件名称
            String newFileName = fileName + fileType;
            // 文件上传到阿里云
            client.putObject(bucketName, newFileName, inputStream);

            client.shutdown();
            //获取url地址
            uploadUrl = "http://" + bucketName + "." + endPoint + "/" + newFileName;
        } catch (IOException e) {
            LOGGER.error("讲师头像上传异常，文件名称：{}" + file.getOriginalFilename() + "异常信息：{}" + e.getMessage());
            throw new GuLiException(ResultCode.FILE_UPLOAD_ERROR.getStatus(), e.getMessage());
        }
        return uploadUrl;
    }

}
