package com.lms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.entity.LmsUserInfo;
import com.lms.service.LmsUserInfoService;
import com.lms.mapper.LmsUserInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lqq21
* @description 针对表【lms_user_info】的数据库操作Service实现
* @createDate 2023-03-25 17:17:10
*/
@Service
public class LmsUserInfoServiceImpl extends ServiceImpl<LmsUserInfoMapper, LmsUserInfo>
    implements LmsUserInfoService{

    @Override
    public void saveUser(LmsUserInfo userInfo) {
        this.save(userInfo);
    }

    @Override
    public List<LmsUserInfo> findAll() {
        return this.list();
    }


}




