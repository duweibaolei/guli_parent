package com.dwl.service_oss.controller;

import com.dwl.common_utils.Result;
import com.dwl.common_utils.StringUtil;
import com.dwl.service_oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api("阿里云文件管理")
@CrossOrigin // 跨域
@RestController
@RequestMapping("oss/file")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 阿里云oss文件上传
     * @param file 对应文件
     * @param host 对应的上传类型的文件夹名称
     * @return 成功返回上传成功的路径信息，失败返回失败信息。
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public Result upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(name = "host", value = "对应文件夹名称", required = true)
                    String host) {
        // 校验前端是否存在对应目录信息，如果没有则需要进行添加
        if(StringUtil.isEmpty(host)){
            return Result.error().message("路径异常，请联系相关人员！");
        }
        try {
            // 文件开始上传
            String uploadUrl = fileService.upload(file, host);
            return Result.ok().message("文件上传成功").data("url", uploadUrl);
        }catch (Exception e){
            return Result.error().message("文件上传异常，异常信息：" + e.getMessage());
        }
    }
}
