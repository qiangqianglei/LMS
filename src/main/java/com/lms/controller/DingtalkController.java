package com.lms.controller;

import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.lms.service.impl.DingtalkServiceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shade.com.alibaba.fastjson2.JSON;

import java.net.URLEncoder;

@Controller
@RequestMapping("/dingtalk")
@Slf4j
public class DingtalkController {

    public static com.aliyun.dingtalkoauth2_1_0.Client authClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    //接口地址：注意/auth与钉钉登录与分享的回调域名地址一致
    @GetMapping(value = "/auth")
    public String auth() throws Exception {
        // 构建重定向url
        String thirdPartyUrl = buildThirdPartyUrl();
        log.info("重定向：{}", thirdPartyUrl);
        return "redirect:" + thirdPartyUrl;
    }
    public static com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }
    /**
     * 获取用户个人信息
     * @param accessToken
     * @return
     * @throws Exception
     */
    public String getUserinfo(String accessToken) throws Exception {
        com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
        GetUserHeaders getUserHeaders = new GetUserHeaders();
        getUserHeaders.xAcsDingtalkAccessToken = accessToken;
        //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
        String me = JSON.toJSONString(client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions()).getBody());
        System.out.println(me);
        return me;
    }


    @GetMapping(value = "/login")
    public String login(@RequestParam(value = "authCode")String authCode) throws Exception {
        com.aliyun.dingtalkoauth2_1_0.Client client = authClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()

                //应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
                .setClientId(DingtalkServiceServiceImpl.accessKey)

                //应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
                .setClientSecret(DingtalkServiceServiceImpl.accessSecret)
                .setCode(authCode)
                .setGrantType("authorization_code");
        GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
        //获取用户个人token
        String accessToken = getUserTokenResponse.getBody().getAccessToken();
        return  getUserinfo(accessToken);
    }

    private String buildThirdPartyUrl() throws Exception{
        String redirectUrl = "http://10.124.200.2:18080/dingtalk/login";
        redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        String resultUrl = "https://login.dingtalk.com/oauth2/auth?redirect_uri=" + redirectUrl + "&response_type=code&client_id="
                + DingtalkServiceServiceImpl.accessKey +"&scope=openid&state=dddd&prompt=consent";
        log.info("构建Url:{}", resultUrl);
        return resultUrl;
    }
}
