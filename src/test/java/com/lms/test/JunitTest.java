package com.lms.test;

import com.lms.LMSApplication;
import com.lms.param.GjsslbParam;
import com.lms.service.ApiService;
import com.lms.service.DingtalkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LMSApplication.class)
@Slf4j
public class JunitTest {

    @Autowired
    private DingtalkService dingtalkService;

    @Autowired
    private ApiService apiService;

    @Test
    public void testGetAccessToken() {
        String accessToken = dingtalkService.getAccessToken();
    }

    @Test
    public void testGetDptList() {
        System.out.println(dingtalkService.getUserDetailByUserId("200407370437919863", "59a2c3edcb9738aeafda862e6d40078c"));
    }

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @Test
    public void testGjsslb() {
        String url = "https://pccz.court.gov.cn/pcajxxw/searchKey/gjsslb";
        GjsslbParam gjsslbParam = new GjsslbParam();
        gjsslbParam.setAjlx("999");
        gjsslbParam.setSearch("正平路桥");
        gjsslbParam.setPageNum(1);

        scheduler.schedule(() -> {
            ResponseEntity<String> stringResponseEntity = apiService.postJson(url, gjsslbParam);
            String body = stringResponseEntity.getBody();
            //log.info("接口调用返回值:{}",body);
            if (!body.contains("共搜索出7条记录")) {
                log.info("破产网出新公告了！");
            } else {
                log.info("破产网没出新公告。。");
            }
        }, 1, TimeUnit.MINUTES);
    }
}
