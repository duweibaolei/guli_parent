package com.dwl.service_edu.controller;

import com.dwl.common_utils.Result.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin // 解决跨域
@RequestMapping("/eduService/user")
public class EduLoginController {

    @PostMapping("/login")
    public Result login() {
        return Result.ok().data("token", "admin");
    }

    @GetMapping("/info")
    public Result info() {
        return Result.ok().data("roles", "admin").data("name", "admin").data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
