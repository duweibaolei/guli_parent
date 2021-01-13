package com.dwl.common_utils;

/**
 * 定义统一返回码类
 */
public enum ResultCode {

    SUCCESS(20000), // 成功
    ERROR(50000), // 失败
    FILE_UPLOAD_ERROR(60000);

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
