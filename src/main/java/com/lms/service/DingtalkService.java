package com.lms.service;

import com.lms.vo.SysResult;

public interface DingtalkService {
    String getAccessToken();

    String getUserDetailByUserId(String userId, String accessToken);
}
