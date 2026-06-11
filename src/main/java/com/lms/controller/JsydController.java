package com.lms.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.lms.entity.ParamTest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class JsydController {

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
    public JSONObject testJsydAuth(@RequestBody JSONObject paramJson) {
        String loginAcct = paramJson.getStr("loginAcct");
        String acctPwd = paramJson.getStr("acctPwd");
        String mobile = paramJson.getStr("mobile");
        JSONObject result = new JSONObject();

        if ("xgmm".equals(acctPwd)) {
            result.set("code", "232295");
        } else {
            result.set("code", "000000");
        }
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

    /**
     * { "code": "000000", "msg": "请求成功", "data": null, "exception": null }
     * @return JSONObject 响应结果
     */
    @PostMapping("/js-cm-csc/imc/user/resetPwd")
    public JSONObject testResetPwd() {
        JSONObject result = new JSONObject();
        result.set("code", "000000");
        result.set("msg", "请求成功");
        result.set("data", null);
        result.set("exception", null);
        return result;
    }
}
