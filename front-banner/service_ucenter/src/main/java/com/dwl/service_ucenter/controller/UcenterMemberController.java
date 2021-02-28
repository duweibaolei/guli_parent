package com.dwl.service_ucenter.controller;


import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.ordervo.UcenterMemberOrder;
import com.dwl.common_utils.util.BeanUtil;
import com.dwl.common_utils.util.JwtUtil;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.util.RedisUtils;
import com.dwl.service_ucenter.entity.UcenterMember;
import com.dwl.service_ucenter.entity.vo.LoginInfoVo;
import com.dwl.service_ucenter.entity.vo.LoginVo;
import com.dwl.service_ucenter.entity.vo.RegisterVo;
import com.dwl.service_ucenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * redis服务类
     */
    private final RedisUtils redisUtils;

    @Autowired
    public UcenterMemberController(UcenterMemberService memberService, RedisUtils redisUtils) {
        this.memberService = memberService;
        this.redisUtils = redisUtils;
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
    @GetMapping("/getLoginInfo")
    public Result getLoginInfo(
            @ApiParam(name = "request", value = "会员登录信息", required = true) HttpServletRequest request) {
        String id = JwtUtil.getMemberIdByJwtToken(request);
        try {
            if (StringUtil.isEmpty(id)) {
                return Result.error().message("会员登录信息，从token中获取id为空！");
            }
            // 查询redis中是否存在，如果没有则查询数据库
            LoginInfoVo infoVo = (LoginInfoVo) redisUtils.get("member_openid");
            if (BeanUtil.isEmpty(infoVo)) {
                infoVo = memberService.loginInfo(id);
            }
            return Result.ok().data("info", infoVo);
        } catch (Exception e) {
            LOGGER.error("获取登录信息异常，异常信息：[{}]", e + "");
            return Result.error().message("获取登录信息异常：" + e);
        }
    }

    /**
     * 根据token获取用户信息
     *
     * @param request
     * @return
     */
    @ApiOperation("根据token获取用户信息")
    @GetMapping("/getMemberInfo")
    public Result getMemberInfo(HttpServletRequest request) {
        // 调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String memberId = JwtUtil.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return Result.ok().data("userInfo", member);
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据用户id获取用户信息")
    @PostMapping("/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        // 把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMember = new UcenterMemberOrder();
        BeanUtils.copyProperties(member, ucenterMember);
        return ucenterMember;
    }

    /**
     * 查询某一天注册人数
     *
     * @param day
     * @return
     */
    @ApiOperation(value = "查询某一天注册人数")
    @GetMapping("/countRegister/{day}")
    public Result countRegister(
            @ApiParam(name = "day", value = "天", required = true) @PathVariable String day) {
        Integer count = memberService.countRegisterDay(day);
        return Result.ok().data("countRegister", count);
    }

}

