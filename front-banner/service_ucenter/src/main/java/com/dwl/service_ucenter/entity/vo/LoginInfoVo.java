package com.dwl.service_ucenter.entity.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录对象实体类
 */
@Api("登录对象实体类")
@Data
public class LoginInfoVo {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;
}
