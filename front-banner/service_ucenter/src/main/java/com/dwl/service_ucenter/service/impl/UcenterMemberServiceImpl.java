package com.dwl.service_ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.BeanUtil;
import com.dwl.common_utils.util.JwtUtil;
import com.dwl.common_utils.util.MD5Util;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_ucenter.entity.UcenterMember;
import com.dwl.service_ucenter.entity.vo.LoginInfoVo;
import com.dwl.service_ucenter.entity.vo.LoginVo;
import com.dwl.service_ucenter.entity.vo.RegisterVo;
import com.dwl.service_ucenter.mapper.UcenterMemberMapper;
import com.dwl.service_ucenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2021-01-29
 */
@Service
@Api("会员表 服务实现类")
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UcenterMemberServiceImpl.class);

    /**
     * 会员登录
     *
     * @param loginVo 登录对象
     * @return 使用JWT生成token字符串
     */
    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String passWord = loginVo.getPassword();
        // 校验登录用户是否为空
        if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(passWord)) {
            LOGGER.info("账号 [{}] 为空。", mobile);
            throw new GuLiException(ResultCode.NULL_ERROR.getStatus(), "账号或者密码为空！");
        }
        // 根据手机号获取会员信息
        UcenterMember member = this.getOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (BeanUtil.isEmpty(member)) {
            LOGGER.info("会员信息不存在，账号 [{}]。", mobile);
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "会员信息不存在！");
        }
        // 校验密码
        if (!MD5Util.encrypt(passWord).equals(member.getPassword())) {
            LOGGER.info("会员信息密码错误，账号 [{}]。", mobile);
            throw new GuLiException(ResultCode.ERROR.getStatus(), "会员信息密码错误！");
        }
        // 校验是否被禁用
        if (member.getIsDisabled() == 1) {
            LOGGER.info("会员已经被禁用，账号 [{}]。", mobile);
            throw new GuLiException(ResultCode.ERROR.getStatus(), "会员已经被禁用！");
        }
        return JwtUtil.getJwtToken(member.getId(), member.getNickname());
    }

    /**
     * 会员注册
     *
     * @param registerVo 注册对象
     */
    @Override
    public void register(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String passWord = registerVo.getPassword();
        String nickname = registerVo.getNickname();
        // 校验注册用户是否为空
        if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(passWord) ||
                StringUtil.isEmpty(nickname)) {
            LOGGER.info("会员注册信不全，账号 [{}]。", mobile);
            throw new GuLiException(ResultCode.NULL_ERROR.getStatus(), "会员注册信不全，请填写完整！");
        }
        // 查询数据库中是否存在相同的手机号码
        UcenterMember member = this.getOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (BeanUtil.isNotEmpty(member)) {
            LOGGER.info("该手机号码已经注册过会员，账号 [{}]。", mobile);
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "该手机号码已经注册过会员！");
        }
        member = new UcenterMember();
        BeanUtils.copyProperties(registerVo, member);
        member.setPassword(MD5Util.encrypt(passWord));
        int a = baseMapper.insert(member);
        if (a <= 0) {
            LOGGER.info("用户注册保存异常，账号 [{}]。", mobile);
            throw new GuLiException(ResultCode.SAVE_ERROR.getStatus(), "用户注册保存异常！");
        }
    }

    /**
     * 根据 id 获取登录对象信息
     *
     * @param id
     * @return
     */
    @Override
    public LoginInfoVo loginInfo(String id) {
        UcenterMember member = this.getById(id);
        if (BeanUtil.isEmpty(member)) {
            LOGGER.error("登录对象信息为空：[{}]", id);
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "登录对象信息为空！");
        }
        LoginInfoVo infoVo = new LoginInfoVo();
        BeanUtils.copyProperties(member, infoVo);
        return infoVo;
    }

    /**
     * 微信扫码登录保存用户信息
     *
     * @param openid      扫描人凭证id
     * @param userInfoMap 从微信获取的用户信息
     * @return UcenterMember
     */
    @Override
    public UcenterMember saveUcEnterMember(String openid, HashMap userInfoMap) {
        if (BeanUtil.isEmpty(userInfoMap)) {
            throw new GuLiException(ResultCode.NULL_ERROR.getStatus(), "获取微信信息为空！");
        }
        UcenterMember member = new UcenterMember();
        member.setOpenid(openid);
        member.setNickname((String) userInfoMap.get("nickname"));
        member.setAvatar((String) userInfoMap.get("headimgurl"));
//        member.setSex((int) userInfoMap.get("sex"));
        baseMapper.insert(member);
        return member;
    }

    /**
     * 查询某一天注册人数
     *
     * @param day
     * @return
     */
    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }


}
