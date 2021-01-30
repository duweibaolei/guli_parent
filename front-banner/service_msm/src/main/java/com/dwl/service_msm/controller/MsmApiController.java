package com.dwl.service_msm.controller;

import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.util.RedisUtils;
import com.dwl.service_msm.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/msmService/msmApi")
@CrossOrigin //跨域
@Api("短信接口")
public class MsmApiController {

    public static final Logger LOGGER = LoggerFactory.getLogger(MsmApiController.class);

    /**
     * 发送阿里云短信服务
     */
    private final MsmService msmService;

    /**
     * redis 服务
     */
    private final RedisUtils redisUtils;

    @Autowired
    public MsmApiController(MsmService msmService, RedisUtils redisUtils) {
        this.msmService = msmService;
        this.redisUtils = redisUtils;
    }

    /**
     * 发送短信的方法
     *
     * @param phone 用户手机号
     * @return Result
     */
    @ApiOperation("发送短信的方法")
    @GetMapping("send/{phone}")
    public Result sendMsm(
            @ApiParam(name = "phone", value = "用户手机号", required = true)
            @PathVariable String phone) {
        // 从redis获取验证码，如果获取到直接返回
        String code = (String) redisUtils.get(phone);
        if (!StringUtil.isEmpty(code)) {
            return Result.ok();
        }
        try {
            // 调用service发送短信的方法
            msmService.send(phone, "SMS_180051135");
            /* 发送成功，把发送成功验证码放到redis里面 设置有效时间 */
            redisUtils.set(phone, code, 5L, TimeUnit.MINUTES);
            return Result.ok();
        } catch (Exception e) {
            LOGGER.error("用户手机号 {}发送短信失败：{}", phone, e + "");
            return Result.error().message("短信发送失败");
        }
    }
}