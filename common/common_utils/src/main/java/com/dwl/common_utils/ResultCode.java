package com.dwl.common_utils;

/**
 * 定义统一返回码类
 */
public enum ResultCode {

    SUCCESS(20000), // 成功
    ERROR(50000), // 失败
    FILE_UPLOAD_ERROR(50001), // 文件上传异常
    QUERY_ERROR(50002), // 查询异常
    SAVE_ERROR(50003), // 保存异常
    UPDATA_ERROR(50004), // 更新异常
    DELETED_ERROR(50005); // 删除异常

    private Integer status;

    ResultCode(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
