package com.lms.controller;

import com.lms.entity.LmsUserInfo;
import com.lms.service.LmsUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LmsUserInfoService userInfoService;

    @PostMapping("/save")
    public void save(@RequestBody LmsUserInfo userInfo) {
        userInfoService.save(userInfo);
    }

    @GetMapping("/list")
    public List list() {
        return userInfoService.findAll();
    }
}
