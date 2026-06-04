package com.lms.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import com.lms.entity.ParamTest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    /**
     * {
     *     "USERRSP": {
     *         "BODY": {
     *             "LOGINACCT": "admin",
     *             "RSP": "0",
     *             "RSPMSG": "密码修改成功!"
     *         },
     *         "HEAD": {
     *             "CODE": "",
     *             "SERVICEID": "JSTSM",
     *             "SID": "3",
     *             "TIMESTAMP": "20231108110211"
     *         }
     *     }
     * }
     * @return
     */
    @PostMapping("/js-cm-csc/open/oa/modifyPasswordForOA")
    public JSONObject testJsydResetPwd() {
        JSONObject result = new JSONObject();

        JSONObject userRspJson = new JSONObject();

        JSONObject head = new JSONObject();
        head.set("CODE", "");
        head.set("SERVICEID", "JSTSM");
        head.set("SID", "3");
        head.set("TIMESTAMP", DateUtil.format(new Date(), "yyyyMMddHHMMss"));

        JSONObject body = new JSONObject();
        body.set("LOGINACCT", "lpp");
        body.set("RSP", "0");
        body.set("RSPMSG", "密码修改成功!");

        userRspJson.set("BODY", body);
        userRspJson.set("HEAD", head);

        result.set("USERRSP", userRspJson);

        return result;
    }

    @PostMapping("/authn/aisauth/authAcct")
    public JSONObject testJsydAuth() {
        JSONObject result = new JSONObject();

        result.set("code", "000000");
        result.set("msg", "请求成功");
        result.set("data", true);
        return result;
    }

    /**
     * { "code": "000000", "msg": "请求成功", "data":
     * { "serialNum": "cj05DZQjpp40YtA3oVwNL", "randomCode": "991958" }}
     * @return
     */
    @PostMapping("/authn/aisauth/sendCode")
    public JSONObject testJsydSendCode() {
        JSONObject result = new JSONObject();

        JSONObject data = new JSONObject();
        data.set("serialNum", "cj05DZQjpp40YtA3oVwNL");
        data.set("randomCode", "123456");

        result.set("code", "000000");
        result.set("msg", "请求成功");
        result.set("data", data);
        return result;
    }

    @PostMapping("/openapi/v1/api/user/changePwd")
    public JSONObject testHxResetPwd() {
        JSONObject result = new JSONObject();

        JSONObject data = new JSONObject();
        data.set("reason", "密码修改成功");
        data.set("status", "请忽略");

        result.set("code", "SUCCESS");
        result.set("data", data);
        return result;
    }

    /**
     * 江铜生成图形验证码
     * @return
     */
    @GetMapping("/gateway/user/kaptcha/generate")
    public JSONObject JTGenerateCaptcha() {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        data.set("JpegString", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD...");
        data.set("cookie", "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6");

        result.set("code", 200);
        result.set("message", "success");

        result.set("data", data);
        return result;
    }

    /**
     * 江铜发送重置密码短信
     * @return
     */
    @PostMapping("/gateway/user/loginFoundPassword/messageResetPasswordControl")
    public JSONObject JTSendResetPwdMsg(@RequestBody JSONObject paramJson) {
        JSONObject result = new JSONObject();
        if (paramJson == null) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        String telePhone = paramJson.getStr("telePhone");
        String kaptchaCode = paramJson.getStr("kaptchaCode");
        String kaptcha = paramJson.getStr("kaptcha");

        if (StrUtil.isBlank(telePhone) || StrUtil.isBlank(kaptchaCode) || StrUtil.isBlank(kaptcha)) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        result.set("code", 200);
        result.set("message", "success");
        result.set("data", null);

        return result;
    }

    /**
     * 江铜发送重置密码短信
     * @return
     */
    @PostMapping("/gateway/user/loginFoundPassword/checkVerificationCode")
    public JSONObject JTResetPwd(@RequestBody JSONObject paramJson) {
        JSONObject result = new JSONObject();
        if (paramJson == null) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        String telePhone = paramJson.getStr("telePhone");
        String verificationCode = paramJson.getStr("verificationCode");
        String newPassword = paramJson.getStr("newPassword");
        String confirmPassword = paramJson.getStr("confirmPassword");

        if (StrUtil.isBlank(telePhone) || StrUtil.isBlank(verificationCode)
                || StrUtil.isBlank(newPassword) || StrUtil.isBlank(confirmPassword)) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        result.set("code", 200);
        result.set("message", "success");
        result.set("data", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");

        return result;
    }

    /**
     * 江铜发送登录短信
     * @return
     */
    @PostMapping("/gateway/user/loginByTelePhone/messageSend/{phone}")
    public JSONObject JTSendLoginMsg(@PathVariable String phone, @RequestBody JSONObject paramJson) {
        JSONObject result = new JSONObject();
        if (paramJson == null || StrUtil.isBlank(phone)) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        String telePhone = paramJson.getStr("telePhone");
        String kaptchaCode = paramJson.getStr("kaptchaCode");
        String kaptcha = paramJson.getStr("kaptcha");

        if (StrUtil.isBlank(telePhone) || StrUtil.isBlank(kaptchaCode)
                || StrUtil.isBlank(kaptcha )) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        result.set("code", 200);
        result.set("message", "操作成功");
        result.set("result", "61cce8b3c96a40bda7f278688efee0d4");
        result.set("tracer", "48b630956abd419887c0cf2fe82031e4.95.17783179370580041");
        result.set("success", true);
        return result;
    }


    /**
     * 江铜发送登录短信
     * @return
     */
    @PostMapping("/gateway/user/login")
    public JSONObject JTLogin(@RequestBody JSONObject paramJson) {
        JSONObject result = new JSONObject();
        if (paramJson == null) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        String username = paramJson.getStr("username");
        String password = paramJson.getStr("password");
        String verid = paramJson.getStr("verid");

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)
                || StrUtil.isBlank(verid )) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        result.set("code", 200);
        result.set("message", "success");
        result.set("data", null);
        return result;
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("/gateway/user/gateway/sso/login2")
    public JSONObject JTSsoLogin2(@RequestBody JSONObject paramJson) {
        JSONObject result = new JSONObject();

        String username = paramJson.getStr("username");
        String password = paramJson.getStr("password");
        String kaptchaCode = paramJson.getStr("kaptchaCode");
        String kaptcha = paramJson.getStr("kaptcha");

        if (StrUtil.isBlank(username) || StrUtil.isBlank(kaptchaCode)
                || StrUtil.isBlank(kaptcha ) || StrUtil.isBlank(password)) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        if ("1234".equals(kaptchaCode)) {
            result.set("code", 14001);
            result.set("status", 14001);
            result.set("message", "本次登录使用的是初始默认密码，请修改密码后再登录");
        } else {
            result.set("code", 200);
            result.set("status", 200);
            result.set("message", "登录成功");
        }
        result.set("traceId", "c2b6458660d34056acb6381abe5afe6f.114.17786528303870883");


        JSONObject resultData = new JSONObject();

        resultData.set("ticket", "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbk1lc3NhZ2UiOiLnmbvlvZXmiJDlip8iLCJjb2RlIjoyMDAsImNyZWF0ZV90aW1lIjoxNzc4NjUyODMwNjM0LCJuYW1lIjoi56iL5rabIiwibG9naW5EYXRlIjoxNzc4NjUyODMwNjM0LCJ0aWNrZXRUeXBlIjoidGlja2V0TmV3VGVzdCIsInVzZXJOYW1lIjoiMDAwNTc3NjgiLCJleHAiOjE3Nzg3MzkyMzAsInVzZXJJZCI6MTQ0MjUxLCJhdXRob3JpdGllcyI6MX0.qVugVVSFFgrwTMccsa3m9p80sqOKjCCAzF4GbGo-Zu-i3x6A-yU3uwxXLbac6FaWfe27ANQ5cghFz6l43GM3kQ");
        resultData.set("refreshTicket", "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbk1lc3NhZ2UiOiLnmbvlvZXmiJDlip8iLCJjb2RlIjoyMDAsImNyZWF0ZV90aW1lIjoxNzc4NjUyODMwNjM0LCJuYW1lIjoi56iL5rabIiwibG9naW5EYXRlIjoxNzc4NjUyODMwNjM0LCJ0aWNrZXRUeXBlIjoidGlja2V0TmV3VGVzdCIsInVzZXJOYW1lIjoiMDAwNTc3NjgiLCJleHAiOjE3NzkyNTc2MzAsInVzZXJJZCI6MTQ0MjUxLCJhdXRob3JpdGllcyI6MX0.jomNBzXh1bdcMC0Mq5BwXkNq6SF5byEbq4AePmZyZHuRsN50YEMuECpteQhdGHZWAGJJclsrkEYKy2D3W8Ujfw");
        resultData.set("redirectUrl", "http://newevntestbaseplatform.jxcc.com/");

        result.set("result", resultData);
        return result;
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("/gateway/sso/login")
    public JSONObject JTSsoLogin(@RequestBody JSONObject paramJson) {
        JSONObject resultData = new JSONObject();
        resultData.set("newToken", "BearereyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDA1MjUzMSIsInN5c0NvZGUiOiJqdFZwbiIsImdlbmRlciI6IjEiLCJjcmVhdGVfdGltZSI6MTc3OTg2ODA1Nzk1NSwiZGVwdElkIjo0NjQ3LCJidXNzT3JnYW5JZCI6MSwiZW5hYmxlZCI6dHJ1ZSwiYXV0aG9yaXRpZXMiOjEsInJlTG9naW4iOnRydWUsInBob25lIjoia25rWWNMRittWmpyejFJMVVOaTYxUT09IiwibmFtZSI6Ium7hOWBpSIsImlkIjoyNDMwNCwiZW1haWwiOiIiLCJ1c2VybmFtZSI6IjAwMDUyNTMxIiwiZXhwIjoxNzc5ODg2MDU3fQ._nT-Z41MYmb0NtpUS1oyeFC1bPhfdq1uGbCAVLZlcF7znGp2aJpWSmkXyYILSceSOcIiGQoCy-rwUJJCwjEerw");

// 创建并设置 sysuserorg 数组
        JSONArray sysuserorgArray = new JSONArray();

// 1. 创建第一个sysuserorg对象
        JSONObject sysuserorgItem1 = new JSONObject();
        sysuserorgItem1.set("microAppShow", null);
        sysuserorgItem1.set("syscode", "base");
        sysuserorgItem1.set("uid", 24304);
        sysuserorgItem1.set("menuurl", "/main/system");
        sysuserorgItem1.set("systemType", "groupSystem");
        sysuserorgItem1.set("id", 24950);
        sysuserorgItem1.set("sattribute2", "");
        sysuserorgItem1.set("orgName", "江西铜业股份有限公司");
        sysuserorgItem1.set("sysid", 1);
        sysuserorgItem1.set("organTypeBus", "administrative_organ,union_organ,party_organ,youth_league_organ,budget_organ");
        sysuserorgItem1.set("mobileEntry", "http://newtestexternal.jxcc.com/baseplatformh5");
        sysuserorgItem1.set("entry", "http://newevntestbaseplatform.jxcc.com");
        sysuserorgItem1.set("requireAccessParam", 0);
        sysuserorgItem1.set("sysname", "基础平台");
        sysuserorgItem1.set("buss_organ_id", -1);
        sysuserorgItem1.set("organType", "administrative_organ");
        sysuserorgItem1.set("organHrId", "-1");
        sysuserorgItem1.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem1);

// 2. 创建第二个sysuserorg对象
        JSONObject sysuserorgItem2 = new JSONObject();
        sysuserorgItem2.set("syscode", "pims");
        sysuserorgItem2.set("uid", 24304);
        sysuserorgItem2.set("menuurl", "/main/pims");
        sysuserorgItem2.set("systemType", "groupSystem");
        sysuserorgItem2.set("id", 25518);
        sysuserorgItem2.set("sattribute2", "");
        sysuserorgItem2.set("orgName", "江西铜业股份有限公司");
        sysuserorgItem2.set("sysid", 15);
        sysuserorgItem2.set("organTypeBus", "");
        sysuserorgItem2.set("mobileEntry", "http://pimsapptest.jxcc.com");
        sysuserorgItem2.set("entry", "http://testpims.jxcc.com");
        sysuserorgItem2.set("requireAccessParam", 0);
        sysuserorgItem2.set("sysname", "PIMS");
        sysuserorgItem2.set("buss_organ_id", -1);
        sysuserorgItem2.set("organType", "administrative_organ");
        sysuserorgItem2.set("organHrId", "-1");
        sysuserorgItem2.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem2);

// 3. 创建第三个sysuserorg对象
        JSONObject sysuserorgItem3 = new JSONObject();
        sysuserorgItem3.set("syscode", "cadreSystem");
        sysuserorgItem3.set("uid", 24304);
        sysuserorgItem3.set("menuurl", "/main/cadreSystem");
        sysuserorgItem3.set("systemType", "groupSystem");
        sysuserorgItem3.set("id", 42705);
        sysuserorgItem3.set("sattribute2", "");
        sysuserorgItem3.set("orgName", "铜锐公司");
        sysuserorgItem3.set("sysid", 27);
        sysuserorgItem3.set("organTypeBus", "");
        sysuserorgItem3.set("mobileEntry", "http://newtestcadreapp.jxcc.com");
        sysuserorgItem3.set("entry", "https://newtestcadre.jxcc.com");
        sysuserorgItem3.set("requireAccessParam", 0);
        sysuserorgItem3.set("sysname", "干部管理系统");
        sysuserorgItem3.set("buss_organ_id", 4640);
        sysuserorgItem3.set("organType", "administrative_organ");
        sysuserorgItem3.set("organHrId", "AzAAAACjw0HM567U");
        sysuserorgItem3.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem3);

// 4. 创建第四个sysuserorg对象
        JSONObject sysuserorgItem4 = new JSONObject();
        sysuserorgItem4.set("syscode", "demo");
        sysuserorgItem4.set("uid", 24304);
        sysuserorgItem4.set("systemType", "groupSystem");
        sysuserorgItem4.set("id", 70216);
        sysuserorgItem4.set("sattribute2", "");
        sysuserorgItem4.set("orgName", "江西铜业股份有限公司");
        sysuserorgItem4.set("sysid", 33);
        sysuserorgItem4.set("organTypeBus", "");
        sysuserorgItem4.set("mobileEntry", "");
        sysuserorgItem4.set("entry", "demo");
        sysuserorgItem4.set("requireAccessParam", 0);
        sysuserorgItem4.set("sysname", "模板工程demo");
        sysuserorgItem4.set("buss_organ_id", -1);
        sysuserorgItem4.set("organType", "administrative_organ");
        sysuserorgItem4.set("organHrId", "-1");
        sysuserorgItem4.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem4);

// 5. 创建第五个sysuserorg对象
        JSONObject sysuserorgItem5 = new JSONObject();
        sysuserorgItem5.set("syscode", "qmysapp");
        sysuserorgItem5.set("uid", 24304);
        sysuserorgItem5.set("menuurl", "");
        sysuserorgItem5.set("systemType", "groupSystem");
        sysuserorgItem5.set("id", 24951);
        sysuserorgItem5.set("sattribute2", "");
        sysuserorgItem5.set("orgName", "江铜集团");
        sysuserorgItem5.set("sysid", 35);
        sysuserorgItem5.set("mobileIcon", "https://external.jxcc.com/filePreview/group1/M00/00/5F/CmO0o2Xxq-KAT745AAANuqADA-k946.png");
        sysuserorgItem5.set("organTypeBus", "");
        sysuserorgItem5.set("mobileEntry", "https://ysapp.jxcc.com:6003");
        sysuserorgItem5.set("entry", "aaa");
        sysuserorgItem5.set("requireAccessParam", 0);
        sysuserorgItem5.set("sysname", "全面预算");
        sysuserorgItem5.set("buss_organ_id", 1);
        sysuserorgItem5.set("organType", "administrative_organ");
        sysuserorgItem5.set("organHrId", "00000000-0000-0000-0000-000000000000CCE7AED4");
        sysuserorgItem5.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem5);

// 6. 创建第六个sysuserorg对象
        JSONObject sysuserorgItem6 = new JSONObject();
        sysuserorgItem6.set("syscode", "oaGateway");
        sysuserorgItem6.set("uid", 24304);
        sysuserorgItem6.set("menuurl", "");
        sysuserorgItem6.set("systemType", "groupSystem");
        sysuserorgItem6.set("id", 42934);
        sysuserorgItem6.set("sattribute2", "");
        sysuserorgItem6.set("orgName", "铜锐公司");
        sysuserorgItem6.set("sysid", 179);
        sysuserorgItem6.set("organTypeBus", "administrative_organ");
        sysuserorgItem6.set("mobileEntry", "http://newtestoaemail.jxcc.com");
        sysuserorgItem6.set("entry", "http://newtestoagateway.jxcc.com");
        sysuserorgItem6.set("requireAccessParam", 0);
        sysuserorgItem6.set("sysname", "江铜数字化办公平台");
        sysuserorgItem6.set("buss_organ_id", 4640);
        sysuserorgItem6.set("organType", "administrative_organ");
        sysuserorgItem6.set("organHrId", "AzAAAACjw0HM567U");
        sysuserorgItem6.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem6);

// 7. 创建第七个sysuserorg对象
        JSONObject sysuserorgItem7 = new JSONObject();
        sysuserorgItem7.set("syscode", "ytmedia");
        sysuserorgItem7.set("uid", 24304);
        sysuserorgItem7.set("systemType", "groupSystem");
        sysuserorgItem7.set("id", 42683);
        sysuserorgItem7.set("sattribute2", "");
        sysuserorgItem7.set("orgName", "永平铜矿");
        sysuserorgItem7.set("sysid", 188);
        sysuserorgItem7.set("organTypeBus", "administrative_organ");
        sysuserorgItem7.set("mobileEntry", "http://newtestyptkmediah5.jxcc.com/");
        sysuserorgItem7.set("entry", "http://testyptkmedia.jxcc.com/");
        sysuserorgItem7.set("requireAccessParam", 0);
        sysuserorgItem7.set("sysname", "永铜融媒体");
        sysuserorgItem7.set("buss_organ_id", 2272);
        sysuserorgItem7.set("organType", "administrative_organ");
        sysuserorgItem7.set("organHrId", "Ge62HiFVSmyijCv5A5MSZ8znrtQ=");
        sysuserorgItem7.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem7);

// 8. 创建第八个sysuserorg对象
        JSONObject sysuserorgItem8 = new JSONObject();
        sysuserorgItem8.set("syscode", "mseSystem");
        sysuserorgItem8.set("uid", 24304);
        sysuserorgItem8.set("systemType", "externalSystem");
        sysuserorgItem8.set("id", 42502);
        sysuserorgItem8.set("sattribute2", "");
        sysuserorgItem8.set("orgName", "江铜铅锌");
        sysuserorgItem8.set("sysid", 186);
        sysuserorgItem8.set("organTypeBus", "administrative_organ");
        sysuserorgItem8.set("mobileEntry", "http://10.99.190.27:30778");
        sysuserorgItem8.set("entry", "http://test-mse.jxcc.com");
        sysuserorgItem8.set("requireAccessParam", 0);
        sysuserorgItem8.set("sysname", "设备全生命周期管理系统");
        sysuserorgItem8.set("buss_organ_id", 3735);
        sysuserorgItem8.set("organType", "administrative_organ");
        sysuserorgItem8.set("organHrId", "agbBYkdkTwKHyDf4fMxoOMznrtQ=");
        sysuserorgItem8.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem8);

// 9. 创建第九个sysuserorg对象
        JSONObject sysuserorgItem9 = new JSONObject();
        sysuserorgItem9.set("syscode", "unionSystem");
        sysuserorgItem9.set("uid", 24304);
        sysuserorgItem9.set("menuurl", "/main/unionSystem");
        sysuserorgItem9.set("systemType", "groupSystem");
        sysuserorgItem9.set("id", 69705);
        sysuserorgItem9.set("sattribute2", "");
        sysuserorgItem9.set("orgName", "江西铜业股份有限公司");
        sysuserorgItem9.set("sysid", 8);
        sysuserorgItem9.set("organTypeBus", "");
        sysuserorgItem9.set("mobileEntry", "http://10.99.245.81:9020");
        sysuserorgItem9.set("entry", "http://testjthlh.jxcc.com/");
        sysuserorgItem9.set("requireAccessParam", 0);
        sysuserorgItem9.set("sysname", "合理化建议");
        sysuserorgItem9.set("buss_organ_id", -1);
        sysuserorgItem9.set("organType", "administrative_organ");
        sysuserorgItem9.set("organHrId", "-1");
        sysuserorgItem9.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem9);

// 10. 创建第十个sysuserorg对象
        JSONObject sysuserorgItem10 = new JSONObject();
        sysuserorgItem10.set("syscode", "deviceBaseplatform");
        sysuserorgItem10.set("uid", 24304);
        sysuserorgItem10.set("menuurl", "/");
        sysuserorgItem10.set("id", 78535);
        sysuserorgItem10.set("sattribute2", "");
        sysuserorgItem10.set("orgName", "江铜集团");
        sysuserorgItem10.set("sysid", 273);
        sysuserorgItem10.set("organTypeBus", "");
        sysuserorgItem10.set("mobileEntry", "http://10.99.241.226:8670/");
        sysuserorgItem10.set("entry", "http://testdevice.jxcc.com");
        sysuserorgItem10.set("requireAccessParam", 0);
        sysuserorgItem10.set("sysname", "统一视频平台");
        sysuserorgItem10.set("buss_organ_id", 1);
        sysuserorgItem10.set("organType", "administrative_organ");
        sysuserorgItem10.set("organHrId", "00000000-0000-0000-0000-000000000000CCE7AED4");
        sysuserorgItem10.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem10);

// 11. 创建第十一个sysuserorg对象
        JSONObject sysuserorgItem11 = new JSONObject();
        sysuserorgItem11.set("syscode", "assistant");
        sysuserorgItem11.set("uid", 24304);
        sysuserorgItem11.set("menuurl", "assistant");
        sysuserorgItem11.set("systemType", "externalSystem");
        sysuserorgItem11.set("id", 45101);
        sysuserorgItem11.set("sattribute2", "");
        sysuserorgItem11.set("orgName", "铜锐公司");
        sysuserorgItem11.set("sysid", 220);
        sysuserorgItem11.set("organTypeBus", "administrative_organ");
        sysuserorgItem11.set("mobileEntry", "assistant");
        sysuserorgItem11.set("entry", "assistant");
        sysuserorgItem11.set("requireAccessParam", 0);
        sysuserorgItem11.set("sysname", "铜锐语音助手");
        sysuserorgItem11.set("buss_organ_id", 4640);
        sysuserorgItem11.set("organType", "administrative_organ");
        sysuserorgItem11.set("organHrId", "AzAAAACjw0HM567U");
        sysuserorgItem11.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem11);

// 12. 创建第十二个sysuserorg对象
        JSONObject sysuserorgItem12 = new JSONObject();
        sysuserorgItem12.set("syscode", "jtaqhbSystem");
        sysuserorgItem12.set("uid", 24304);
        sysuserorgItem12.set("menuurl", "/main/jtaqhbSystem");
        sysuserorgItem12.set("systemType", "groupSystem");
        sysuserorgItem12.set("id", 43761);
        sysuserorgItem12.set("sattribute2", "");
        sysuserorgItem12.set("orgName", "铜锐公司");
        sysuserorgItem12.set("sysid", 218);
        sysuserorgItem12.set("organTypeBus", "jtaqhb_organ,administrative_organ");
        sysuserorgItem12.set("mobileEntry", "http://testaqhb.jxcc.com/aqhbh5/");
        sysuserorgItem12.set("entry", "http://testaqhb.jxcc.com/aqjg_v2");
        sysuserorgItem12.set("requireAccessParam", 0);
        sysuserorgItem12.set("sysname", "江铜集团安全环保数字化平台");
        sysuserorgItem12.set("buss_organ_id", 4640);
        sysuserorgItem12.set("organType", "administrative_organ");
        sysuserorgItem12.set("organHrId", "AzAAAACjw0HM567U");
        sysuserorgItem12.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem12);

// 13. 创建第十三个sysuserorg对象
        JSONObject sysuserorgItem13 = new JSONObject();
        sysuserorgItem13.set("syscode", "aiPmSystem");
        sysuserorgItem13.set("uid", 24304);
        sysuserorgItem13.set("menuurl", "/main/pmSystem");
        sysuserorgItem13.set("systemType", "trSystem");
        sysuserorgItem13.set("id", 50265);
        sysuserorgItem13.set("sattribute2", "");
        sysuserorgItem13.set("orgName", "铜锐公司");
        sysuserorgItem13.set("sysid", 242);
        sysuserorgItem13.set("organTypeBus", "administrative_organ");
        sysuserorgItem13.set("mobileEntry", "http://testaipmh5.jxcc.com");
        sysuserorgItem13.set("entry", "http://testaipm.jxcc.com");
        sysuserorgItem13.set("requireAccessParam", 0);
        sysuserorgItem13.set("sysname", "项目管理系统二期");
        sysuserorgItem13.set("buss_organ_id", 4640);
        sysuserorgItem13.set("organType", "administrative_organ");
        sysuserorgItem13.set("organHrId", "AzAAAACjw0HM567U");
        sysuserorgItem13.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem13);

// 14. 创建第十四个sysuserorg对象
        JSONObject sysuserorgItem14 = new JSONObject();
        sysuserorgItem14.set("syscode", "hrThirdStudy");
        sysuserorgItem14.set("uid", 24304);
        sysuserorgItem14.set("menuurl", "/main/hrThirdStudy");
        sysuserorgItem14.set("systemType", "externalSystem");
        sysuserorgItem14.set("id", 151105);
        sysuserorgItem14.set("sattribute2", "");
        sysuserorgItem14.set("orgName", "江铜集团");
        sysuserorgItem14.set("sysid", 268);
        sysuserorgItem14.set("organTypeBus", "");
        sysuserorgItem14.set("mobileEntry", "http://jobhr.jxcc.com/mobile.html?form=nbj_user_mob_home_new");
        sysuserorgItem14.set("entry", "h");
        sysuserorgItem14.set("requireAccessParam", 1);
        sysuserorgItem14.set("sysname", "学习平台");
        sysuserorgItem14.set("buss_organ_id", 1);
        sysuserorgItem14.set("organType", "administrative_organ");
        sysuserorgItem14.set("organHrId", "00000000-0000-0000-0000-000000000000CCE7AED4");
        sysuserorgItem14.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem14);

// 15. 创建第十五个sysuserorg对象
        JSONObject sysuserorgItem15 = new JSONObject();
        sysuserorgItem15.set("syscode", "jtDigitalHrNotice");
        sysuserorgItem15.set("uid", 24304);
        sysuserorgItem15.set("menuurl", "/main/jtDigitalHr");
        sysuserorgItem15.set("systemType", "externalSystem");
        sysuserorgItem15.set("id", 151104);
        sysuserorgItem15.set("sattribute2", "");
        sysuserorgItem15.set("orgName", "江铜集团");
        sysuserorgItem15.set("sysid", 260);
        sysuserorgItem15.set("organTypeBus", "");
        sysuserorgItem15.set("mobileEntry", "http://hr3.jxcc.com");
        sysuserorgItem15.set("entry", "http");
        sysuserorgItem15.set("requireAccessParam", 1);
        sysuserorgItem15.set("sysname", "数字人力待办");
        sysuserorgItem15.set("buss_organ_id", 1);
        sysuserorgItem15.set("organType", "administrative_organ");
        sysuserorgItem15.set("organHrId", "00000000-0000-0000-0000-000000000000CCE7AED4");
        sysuserorgItem15.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem15);

// 16. 创建第十六个sysuserorg对象
        JSONObject sysuserorgItem16 = new JSONObject();
        sysuserorgItem16.set("syscode", "partyConstructionSystem");
        sysuserorgItem16.set("uid", 24304);
        sysuserorgItem16.set("menuurl", "");
        sysuserorgItem16.set("systemType", "groupSystem");
        sysuserorgItem16.set("id", 43061);
        sysuserorgItem16.set("sattribute2", "");
        sysuserorgItem16.set("orgName", "中共江西铜锐信息技术有限公司支部委员会");
        sysuserorgItem16.set("sysid", 176);
        sysuserorgItem16.set("mobileIcon", "https://external.jxcc.com/filePreview/group1/M00/00/5F/CmO0o2Xxq-KAT745AAANuqADA-k946.png");
        sysuserorgItem16.set("organTypeBus", "administrative_organ,union_organ,party_organ,youth_league_organ");
        sysuserorgItem16.set("mobileEntry", "http://testpcph5.jxcc.com");
        sysuserorgItem16.set("entry", "http://testpcp.jxcc.com");
        sysuserorgItem16.set("requireAccessParam", 0);
        sysuserorgItem16.set("sysname", "江铜数字党建系统");
        sysuserorgItem16.set("pcIcon", "https://external.jxcc.com/filePreview/group1/M00/00/5F/CmO0o2Xxq-KAT745AAANuqADA-k946.png");
        sysuserorgItem16.set("buss_organ_id", 12692);
        sysuserorgItem16.set("organType", "party_organ");
        sysuserorgItem16.set("organHrId", "804352893367877648");
        sysuserorgItem16.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem16);

// 17. 创建第十七个sysuserorg对象
        JSONObject sysuserorgItem17 = new JSONObject();
        sysuserorgItem17.set("syscode", "partyConstructionSystem");
        sysuserorgItem17.set("uid", 24304);
        sysuserorgItem17.set("menuurl", "");
        sysuserorgItem17.set("systemType", "groupSystem");
        sysuserorgItem17.set("id", 42523);
        sysuserorgItem17.set("sattribute2", "");
        sysuserorgItem17.set("orgName", "铜锐公司");
        sysuserorgItem17.set("sysid", 176);
        sysuserorgItem17.set("mobileIcon", "https://external.jxcc.com/filePreview/group1/M00/00/5F/CmO0o2Xxq-KAT745AAANuqADA-k946.png");
        sysuserorgItem17.set("organTypeBus", "administrative_organ,union_organ,party_organ,youth_league_organ");
        sysuserorgItem17.set("mobileEntry", "http://testpcph5.jxcc.com");
        sysuserorgItem17.set("entry", "http://testpcp.jxcc.com");
        sysuserorgItem17.set("requireAccessParam", 0);
        sysuserorgItem17.set("sysname", "江铜数字党建系统");
        sysuserorgItem17.set("pcIcon", "https://external.jxcc.com/filePreview/group1/M00/00/5F/CmO0o2Xxq-KAT745AAANuqADA-k946.png");
        sysuserorgItem17.set("buss_organ_id", 4640);
        sysuserorgItem17.set("organType", "administrative_organ");
        sysuserorgItem17.set("organHrId", "AzAAAACjw0HM567U");
        sysuserorgItem17.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem17);

// 18. 创建第十八个sysuserorg对象
        JSONObject sysuserorgItem18 = new JSONObject();
        sysuserorgItem18.set("syscode", "jtDigitalHr");
        sysuserorgItem18.set("uid", 24304);
        sysuserorgItem18.set("menuurl", "/main/jtDigitalHr");
        sysuserorgItem18.set("systemType", "externalSystem");
        sysuserorgItem18.set("id", 151103);
        sysuserorgItem18.set("sattribute2", "");
        sysuserorgItem18.set("orgName", "江铜集团");
        sysuserorgItem18.set("sysid", 255);
        sysuserorgItem18.set("organTypeBus", "");
        sysuserorgItem18.set("mobileEntry", "http://jobhr.jxcc.com/mobile.html?app=hraia&form=hrobs_mob_aiportalhome");
        sysuserorgItem18.set("entry", "http://10.99.190.94:8022");
        sysuserorgItem18.set("requireAccessParam", 1);
        sysuserorgItem18.set("sysname", "数字人力");
        sysuserorgItem18.set("buss_organ_id", 1);
        sysuserorgItem18.set("organType", "administrative_organ");
        sysuserorgItem18.set("organHrId", "00000000-0000-0000-0000-000000000000CCE7AED4");
        sysuserorgItem18.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem18);

// 19. 创建第十九个sysuserorg对象
        JSONObject sysuserorgItem19 = new JSONObject();
        sysuserorgItem19.set("syscode", "evaluation");
        sysuserorgItem19.set("uid", 24304);
        sysuserorgItem19.set("menuurl", "/main/evaluation");
        sysuserorgItem19.set("systemType", "groupSystem");
        sysuserorgItem19.set("id", 151041);
        sysuserorgItem19.set("sattribute2", "");
        sysuserorgItem19.set("orgName", "江西铜业股份有限公司");
        sysuserorgItem19.set("sysid", 253);
        sysuserorgItem19.set("organTypeBus", "administrative_organ");
        sysuserorgItem19.set("mobileEntry", "http://testevaluationmobile.jxcc.com");
        sysuserorgItem19.set("entry", "testevaluation.jxcc.com");
        sysuserorgItem19.set("requireAccessParam", 0);
        sysuserorgItem19.set("sysname", "江铜多维动态分类考核评价监测系统");
        sysuserorgItem19.set("buss_organ_id", -1);
        sysuserorgItem19.set("organType", "administrative_organ");
        sysuserorgItem19.set("organHrId", "-1");
        sysuserorgItem19.set("username", "00052531");
        sysuserorgArray.add(sysuserorgItem19);

// 将完整的数组设置到resultData中
        resultData.set("sysuserorg", sysuserorgArray);

// 创建并设置 sysuser 对象
        JSONObject sysuserObj = new JSONObject();
        sysuserObj.set("lastSyncTime", 1778571165000L);
        sysuserObj.set("hrLastUpdateTime", 1776475189000L);
        sysuserObj.set("highestAcademicSubject", "大学本科");
        sysuserObj.set("type", "");
        sysuserObj.set("userface", "");
        sysuserObj.set("factorsAuth", 0);
        sysuserObj.set("password", "2a10$nTpiPnev3eYQh3pmY.wuouo3gOgtOmgiQYAsEWy.C.P1QJJpCE/kq");
        sysuserObj.set("positionalTitlesLevel", "");
        sysuserObj.set("joinDate", "2022-03-16 08:00:00");
        sysuserObj.set("hrid", "AzAAAAD6gDOA733t");
        sysuserObj.set("isHrData", 1);
// 设置 allDeptId 数组
        JSONArray allDeptIdArray = new JSONArray();
        JSONObject deptItem = new JSONObject();
        deptItem.set("positionCode", "ZW01470064");
        deptItem.set("isrespposition", "1");
        deptItem.set("delFlag", "0");
        deptItem.set("positionName", "研发工程师");
        deptItem.set("organExt13", "-1_1_4640");
        deptItem.set("organExt14", "江西铜业股份有限公司_江铜集团_铜锐公司");
        deptItem.set("organExt15", "26220");
        deptItem.set("hrid", "9ShrkKRRRo67JEccMnXiB3SuYS4=");
        deptItem.set("id", 14525);
        deptItem.set("strId", 4647);
        deptItem.set("parentId", 14395);
        deptItem.set("organName", "研发中心");
        deptItem.set("snumber", "014707");
        deptItem.set("organType", "administrative_organ");
        allDeptIdArray.add(deptItem);
        sysuserObj.set("allDeptId", allDeptIdArray);
        sysuserObj.set("id", 24304);
        sysuserObj.set("ext1", "");
        sysuserObj.set("ethnic", "汉族");
        sysuserObj.set("deptId", 4647);
        sysuserObj.set("telephone", "groPaodWOh3C5YxqmOSeHw==");
        sysuserObj.set("highestAcademicSchool", "江西师范大学");
        sysuserObj.set("identityCard", "t5WAQpuVl45iB8DLWSWUhFwIEqBE1dvl");
        sysuserObj.set("employmentRelationshipType", "在册在岗");
        sysuserObj.set("isMainSystemRole", 0);
        sysuserObj.set("updateName", "黄健");
        sysuserObj.set("sortCode", "10004199");
        sysuserObj.set("phone", "groPaodWOh3C5YxqmOSeHw==");
        sysuserObj.set("healthStatus", "");
        sysuserObj.set("isOutstandingGraduates", 0);
        sysuserObj.set("name", "黄健");
        sysuserObj.set("createName", "");
        sysuserObj.set("syncEsUpdateTime", "2025-10-11 21:52:08");
        sysuserObj.set("ext15", "");
        sysuserObj.set("ext14", "");
        sysuserObj.set("gender", "1");
        sysuserObj.set("ext13", "正式党员");
        sysuserObj.set("ext12", "2014-12-10 00:00:00");
        sysuserObj.set("ext11", "");
        sysuserObj.set("origin", "");
        sysuserObj.set("ext10", "");
        sysuserObj.set("remark", "http://10.99.186.28:80/group1/M00/00/93/CmO6HGU6AQ6AdhPpAAY-GhOpPdI451.jpg");
        sysuserObj.set("delFlag", 0);
        sysuserObj.set("enabled", true);
        sysuserObj.set("hrUpdateTime", "");
        sysuserObj.set("updateBy", 24304);
        sysuserObj.set("email", "");
        sysuserObj.set("caCertNumber", "");
        sysuserObj.set("address", "CclvDY4PJQRiu8rbRPLhGJtzTAjKqIckOSmMzmbAyvFwoX2rYrHBVA==");
        sysuserObj.set("pawUpdateTime", 1779327904000L);
        sysuserObj.set("managementunit", "");
        sysuserObj.set("firstDegree", "2017-07-01 00:00:00.0");
        sysuserObj.set("positionalTitles", "");
        sysuserObj.set("updateTime", 1778571165000L);
        sysuserObj.set("politicalLandscapeDate", "2016-06-01");
        sysuserObj.set("birthDate", "1995-08-25");
        sysuserObj.set("politicalLandscape", "中共党员");
        sysuserObj.set("highestAcademicGraduationTime", "2017-07-01 00:00:00.0");
        sysuserObj.set("employemodleid", "00000000-0000-0000-0000-000000000002A29E85B3");
        sysuserObj.set("username", "00052531");
        resultData.set("sysuser", sysuserObj);

        resultData.set("code", 200);
        resultData.set("success", true);
        resultData.set("tracer", "1955235d9f5b4d3cbf566fb794c5a4b6.114.17798680579400801");
        resultData.set("message", "操作成功");

        JSONObject result = new JSONObject();
        result.set("result", resultData);

        return resultData;
    }

    /**
     * 江铜发送登录短信
     * @return
     */
    @PostMapping("/gateway/user/user/forceChangePassword")
    public JSONObject JTForceChangePassword(@RequestBody JSONObject paramJson) {
        JSONObject result = new JSONObject();
        if (paramJson == null) {
            result.set("code", 500);
            result.set("message", "false");
            return result;
        }

        String username = paramJson.getStr("username");
        String password = paramJson.getStr("password");
        String oldpassword = paramJson.getStr("oldpassword");
        String confirmpassword = paramJson.getStr("confirmpassword");
        String ticket = paramJson.getStr("ticket");

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)
                || StrUtil.isBlank(oldpassword ) || StrUtil.isBlank(confirmpassword)
                || StrUtil.isBlank(ticket)) {
            result.set("code", 500);
            result.set("message", "参数不合法");
            return result;
        }

        result.set("code", 200);
        result.set("message", "成功");
        result.set("data", null);
        return result;
    }

    public static void main(String[] args) {
        ParamTest resetPwdParam = ParamTest.builder()
                .SERVICEID("11111")
                .LOGINACCT("lei")
                .NEWPASSWORD("2222")
                .build();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("LOGINACCT", resetPwdParam.getLOGINACCT());
        bodyMap.put("NEWPASSWORD", resetPwdParam.getNEWPASSWORD());

        Map<String, Object> headMap = new HashMap<>();
        headMap.put("CODE", resetPwdParam.getCODE());
        headMap.put("SERVICEID", resetPwdParam.getSERVICEID());
        headMap.put("SID", resetPwdParam.getSID());
        headMap.put("TIMESTAMP", resetPwdParam.getTIMESTAMP());

        Map<String, Object> userModifyQeqMap = new HashMap<>();
        userModifyQeqMap.put("BODY", bodyMap);
        userModifyQeqMap.put("HEAD", headMap);

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("USERMODIFYREQ", userModifyQeqMap);

        System.out.println(bodyParams);
    }
}
