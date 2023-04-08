package com.lms.service;

import com.lms.entity.LmsUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lqq21
* @description 针对表【lms_user_info】的数据库操作Service
* @createDate 2023-03-25 17:17:10
*/
public interface LmsUserInfoService extends IService<LmsUserInfo> {

    void saveUser(LmsUserInfo userInfo);

    List<LmsUserInfo> findAll();
}
