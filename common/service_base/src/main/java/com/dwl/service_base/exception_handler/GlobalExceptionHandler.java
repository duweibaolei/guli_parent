package com.dwl.service_base.exception_handler;

import com.dwl.common_utils.Result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 指定出现什么异常执行这个方法
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody // 为了返回数据
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error().message("执行了全局异常处理..");
    }

    /**
     * 特定异常
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.error().message("执行了 ArithmeticException 异常处理..");
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(GuLiException.class)
    @ResponseBody
    public Result error(GuLiException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }
}
