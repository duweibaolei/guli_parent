package com.dwl.service_ucenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dwl.service_ucenter.entity.UcenterMember;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author dwl
 * @since 2021-01-29
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    /**
     * 查询某一天注册人数
     *
     * @param day
     * @return
     */
    Integer countRegisterDay(String day);
}
