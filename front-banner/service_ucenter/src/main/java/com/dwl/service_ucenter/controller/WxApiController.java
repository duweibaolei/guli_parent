package com.dwl.service_ucenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.*;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_base.util.RedisUtils;
import com.dwl.service_ucenter.entity.UcenterMember;
import com.dwl.service_ucenter.service.UcenterMemberService;
import com.dwl.service_ucenter.util.ConstantPropertiesUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@Controller // @RestController会返回数据，而现在只是请求数据，使用@Controller就可以了。
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    /**
     * redis服务类
     */
    private final RedisUtils redisUtils;

    /**
     * 会员表 服务类
     */
    private final UcenterMemberService memberService;

    private static final String HOST_URL = "http://localhost:3000/";

    @Autowired
    public WxApiController(RedisUtils redisUtils, UcenterMemberService memberService) {
        this.redisUtils = redisUtils;
        this.memberService = memberService;
    }


    /**
     * 微信扫描请求生成二维码
     * 微信固定请求路径：String url = "https://open.weixin.qq.com/" +
     * "connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";
     *
     * @return 二维码的地址
     */
    @GetMapping("/login")
    public String genWxCode() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        // 回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; // 获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); // url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuLiException(ResultCode.ERROR.getStatus(), e.getMessage());
        }
        /* 防止csrf攻击（跨站请求伪造攻击）,将值设置在redis中，如果没有对应的值，
           则转到登录页面提示用户重新登录。*/
        String state = MD5Util.encrypt(String.valueOf(MD5Util.randomNumber()));
        redisUtils.set(state, state, 30L, TimeUnit.MINUTES);
        System.out.println("state = " + state);

        // 设置%s里面的值
        String qrcodeUrl = "redirect:" + String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);
        return qrcodeUrl;
    }

    /**
     * 获取扫描人的信息
     * 微信固定的地址："https://api.weixin.qq.com/sns/oauth2/access_token" +
     * "?appid=%s" +
     * "&secret=%s" +
     * "&code=%s" +
     * "&grant_type=authorization_code"
     *
     * @param code  临时票据
     * @param state 验证防止csrf攻击（跨站请求伪造攻击）
     * @return 跳转地址
     */
    @GetMapping("/callback")
    public String callback(String code, String state) {
        String sta = (String) redisUtils.get(state);
        // 验证防止csrf攻击（跨站请求伪造攻击）
        if (StringUtil.isEmpty(sta)) {
            return HOST_URL;
        } else {
            if (!sta.equals(state)) {
                return HOST_URL;
            }
        }
        /* （1）获取code值，临时票据，类似于验证码
           （2）拿着code请求微信固定的地址，得到两个值accsess_token和openid */
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        // 拼接三个参数 ：id秘钥和code值
        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code
        );
        try {
            /* 请求这个拼接好的地址，得到返回两个值accsess_token和openid
                使用httpclient发送请求，得到返回结果 */
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            /*（1）从accessTokenInfo字符串获取出来两个值accsess_token和openid
              （2）把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
              （3）使用json转换工具Gson */
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");
            /* 把扫描人信息添加数据库里面
            判断数据表里面是否存在相同微信信息，根据openid判断 */
            QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("openid", openid);
            queryWrapper.select("id", "openid", "nickname");
            UcenterMember member = memberService.getOne(queryWrapper);
            // memeber是空，表没有相同微信数据，进行添加
            if (BeanUtil.isEmpty(member)) {
                /* 拿着得到accsess_token和openid，再去请求微信提供固定的地址，获取到扫描人信息
                   访问微信的资源服务器，获取用户信息 */
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                // 拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                // 发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                // 获取返回userinfo字符串扫描人信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                member = memberService.saveUcEnterMember(openid, userInfoMap);
            }
            if (StringUtil.isEmpty(member.getId()) || StringUtil.isEmpty(member.getNickname())) {
                throw new GuLiException(ResultCode.ERROR.getStatus(), "获取扫描人的id或者昵称为空！");
            }
            redisUtils.set("member_openid", member, 30L, TimeUnit.MINUTES);
            // 使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtil.getJwtToken(member.getId(), member.getNickname());
            // 返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token=" + jwtToken;
        } catch (Exception e) {
            throw new GuLiException(ResultCode.ERROR.getStatus(), e + "");
        }
    }


}
