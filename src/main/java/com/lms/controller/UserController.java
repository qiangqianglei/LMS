package com.lms.controller;

import com.lms.entity.LmsUserInfo;
import com.lms.service.LmsUserInfoService;
import com.lms.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LmsUserInfoService userInfoService;

    @PostMapping("/save")
    public SysResult save(@RequestBody LmsUserInfo userInfo) {
        userInfoService.save(userInfo);
        return SysResult.success();
    }

    @GetMapping("/list")
    public SysResult list() {
        return SysResult.success(userInfoService.findAll());
    }
}
