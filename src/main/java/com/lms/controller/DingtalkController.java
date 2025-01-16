package com.lms.controller;

import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.lms.service.impl.DingtalkServiceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shade.com.alibaba.fastjson2.JSON;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Enumeration;

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
    public GetUserResponseBody getUserinfo(String accessToken) throws Exception {
        com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
        GetUserHeaders getUserHeaders = new GetUserHeaders();
        getUserHeaders.xAcsDingtalkAccessToken = accessToken;
        //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
        GetUserResponseBody responseBody = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions()).getBody();

        log.info("responseBody:{}", JSON.toJSONString(responseBody));
        return responseBody;
    }


    @GetMapping(value = "/callback")
    public String login(@RequestParam(value = "authCode")String authCode) throws Exception {
        log.info("authCode:{}", authCode);
        String accessKey = "ding5k6i1yz66jmiznhj";
        String accessSecret = "cb51I5XcyNZR6xnOU37sLBy6sdRg3IHuP8vukBVVLZX_CTnPaLBO1sX6kTFZ83_d";
        com.aliyun.dingtalkoauth2_1_0.Client client = authClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()

                //应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
                .setClientId(accessKey)

                //应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
                .setClientSecret(accessSecret)
                .setCode(authCode)
                .setGrantType("authorization_code");
        GetUserTokenResponse getUserTokenResponse = null;
        try {
            getUserTokenResponse = client.getUserToken(getUserTokenRequest);
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                log.error("获取用户token异常，异常信息：{}， {}", err.getCode(), err.getMessage());
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("获取用户token异常，异常信息：{}， {}", err.getCode(), err.getMessage());
            }

        }


        //获取用户个人token
        String accessToken = getUserTokenResponse.getBody().getAccessToken();
        log.info("accessToken:{}", accessToken);
        GetUserResponseBody responseBody = getUserinfo(accessToken);
        return JSON.toJSONString(responseBody);
    }

    private String buildThirdPartyUrl() throws Exception{
        String redirectUrl = "http://10.124.200.2:18080/dingtalk/callback";
        redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        String resultUrl = "https://login.dingtalk.com/oauth2/auth?"
                + "redirect_uri=" + redirectUrl
                + "&response_type=code&client_id="
                + DingtalkServiceServiceImpl.accessKey
                + "&scope=openid&state=dddd&prompt=consent";
        log.info("构建Url:{}", resultUrl);
        return resultUrl;
    }


     @GetMapping(value = "/scanCallback")
    public String scanCallback(HttpServletRequest request) throws Exception {
         Enumeration<String> parameterNames = request.getParameterNames();
         while (parameterNames.hasMoreElements()) {
             String parameterName = parameterNames.nextElement();
             String parameterValue = request.getParameter(parameterName);
             log.info("参数：{}，值：{}", parameterName, parameterValue);
         }
         return "回调成功";
    }


}
