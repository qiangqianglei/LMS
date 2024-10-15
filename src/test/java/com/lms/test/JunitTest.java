package com.lms.test;

import com.lms.LMSApplication;
import com.lms.service.DingtalkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LMSApplication.class)
@Slf4j
public class JunitTest {

    @Autowired
    private DingtalkService dingtalkService;

    @Test
    public void testGetAccessToken() {
        String accessToken = dingtalkService.getAccessToken();
    }

    @Test
    public void testGetDptList() {
        //dingtalkService.getDptList();
    }
}
