package com.lms.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

        JSONObject head = new JSONObject();
        head.set("CODE", "");
        head.set("SERVICEID", "JSTSM");
        head.set("SID", "3");
        head.set("TIMESTAMP", DateUtil.format(new Date(), "yyyyMMddHHMMss"));

        JSONObject body = new JSONObject();
        body.set("LOGINACCT", "lpp");
        body.set("RSP", "0");
        body.set("RSPMSG", "密码修改成功!");

        result.set("BODY", body);
        result.set("HEAD", head);
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
        data.set("randomCode", "991958");

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
}
