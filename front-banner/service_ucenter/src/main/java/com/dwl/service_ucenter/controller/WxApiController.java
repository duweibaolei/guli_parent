package com.dwl.service_ucenter.controller;


import com.dwl.common_utils.Result.ResultCode;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_base.util.RedisUtils;
import com.dwl.service_ucenter.util.ConstantPropertiesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@CrossOrigin
@Controller // @RestController会返回数据，而现在只是请求数据，使用@Controller就可以了。
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    private RedisUtils redisUtils;

    public WxApiController (RedisUtils redisUtils){

    }

    @GetMapping("login")
    public String genWxCode(HttpSession session) {
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
        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);
        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟


        // 设置%s里面的值
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);
        return "redirect:" + qrcodeUrl;
    }

    @GetMapping("/callback")
    public String callback(String code, String state){
        return null;
    }


}
