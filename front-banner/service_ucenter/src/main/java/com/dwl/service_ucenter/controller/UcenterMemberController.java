package com.dwl.service_ucenter.controller;


import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.util.JwtUtil;
import com.dwl.service_ucenter.entity.vo.LoginInfoVo;
import com.dwl.service_ucenter.entity.vo.LoginVo;
import com.dwl.service_ucenter.entity.vo.RegisterVo;
import com.dwl.service_ucenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author dwl
 * @since 2021-01-29
 */
@RestController
@RequestMapping("/serviceUcEnter/ucEnterMember")
@Api("会员表 前端控制器")
public class UcenterMemberController {

    public static final Logger LOGGER = LoggerFactory.getLogger(UcenterMemberController.class);

    /**
     * 会员表服务类
     */
    private final UcenterMemberService memberService;

    public UcenterMemberController(UcenterMemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * @param loginVo 会员登录对象
     * @return Result
     */
    @ApiOperation("会员登录")
    @PostMapping("/login")
    public Result login(
            @ApiParam(name = "loginVo", value = "会员登录对象", required = true)
            @RequestBody LoginVo loginVo) {
        try {

            String token = memberService.login(loginVo);
            LOGGER.info("会员登录：账号 [{}]", loginVo.getMobile());
            return Result.ok().data("token", token);
        } catch (Exception e) {
            LOGGER.info("会员登录异常：账号 [{}]，异常信息：[{}]", loginVo.getMobile(), e + "");
            return Result.error().message("会员登录异常：" + e);
        }
    }

    /**
     * 会员注册
     *
     * @return Result
     */
    @ApiOperation("会员注册")
    @PostMapping("/register")
    public Result register(
            @ApiParam(name = "loginVo", value = "会员登录对象", required = true)
            @RequestBody RegisterVo register) {
        try {
            memberService.register(register);
            return Result.ok().message("注册成功！");
        } catch (Exception e) {
            LOGGER.error("会员注册异常，异常信息：[{}]", e + "");
            return Result.error().message("注册失败：" + e);
        }
    }

    /**
     * 根据token获取登录信息
     *
     * @param request
     * @return Result
     */
    @ApiOperation("根据token获取登录信息")
    @PostMapping("/getLoginInfo")
    public Result getLoginInfo(
            @ApiParam(name = "request", value = "会员登录信息", required = true)
            @RequestBody HttpServletRequest request) {
        String id = JwtUtil.getMemberIdByJwtToken(request);
        try {
            LoginInfoVo infoVo = memberService.loginInfo(id);
            return Result.ok().data("info", infoVo);
        } catch (Exception e) {
            LOGGER.error("获取登录信息异常，异常信息：[{}]", e + "");
            return Result.error().message("获取登录信息异常：" + e);
        }
    }

}

