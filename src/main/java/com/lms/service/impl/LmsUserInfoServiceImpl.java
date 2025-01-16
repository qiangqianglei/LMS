package com.lms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.entity.LmsUserInfo;
import com.lms.service.LmsUserInfoService;
import com.lms.mapper.LmsUserInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

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

    public static void main(String[] args) {
        String version = "我是谁";
        String reg = "^[^\\u4e00-\\u9fa5]+$";
        if (Pattern.compile(reg).matcher(version).find()) {
            System.out.println("校验通过");
        } else {
            System.out.println("校验bu通过");
        }
    }

}




