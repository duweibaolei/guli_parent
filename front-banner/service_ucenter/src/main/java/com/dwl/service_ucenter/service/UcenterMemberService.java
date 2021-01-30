package com.dwl.service_ucenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_ucenter.entity.UcenterMember;
import com.dwl.service_ucenter.entity.vo.LoginInfoVo;
import com.dwl.service_ucenter.entity.vo.LoginVo;
import com.dwl.service_ucenter.entity.vo.RegisterVo;
import io.swagger.annotations.Api;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-01-29
 */
@Api("会员表 服务类")
public interface UcenterMemberService extends IService<UcenterMember> {

    /**
     * @param loginVo 登录对象
     * @return 使用JWT生成token字符串
     */
    String login(LoginVo loginVo);

    /**
     * 会员注册
     *
     * @param registerVo 注册对象
     */
    void register(RegisterVo registerVo);

    /**
     * 根据 id 获取登录对象信息
     *
     * @param id
     * @return
     */
    LoginInfoVo loginInfo(String id);
}
