package com.lms.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkoauth2_1_0.models.GetCorpAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetCorpAccessTokenResponseBody;
import com.aliyun.tea.TeaException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.lms.entity.ZtsOrgGroupInfo;
import com.lms.service.DingtalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DingtalkServiceServiceImpl implements DingtalkService {

    public static final String accessKey = "dingerbu4wwg070r2jpu";
    public static final String accessSecret = "E6i_Us19kLI29Kv97hWqcnB_U0tA865qHCclxUp6eQdkgROO9o7-m4u2oZF0m_fW";
    public static final String corpId = "ding141eac81d161098424f2f5cc6abecb85";
    public static final String apiToken = "229ed268354a37b689b88c915026a82c";





    @Override
    public String getAccessToken() {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(accessKey);
            request.setAppsecret(accessSecret);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            System.out.println(response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
        }
        return null;
    }

    private static String access_token = "d27cc416382a35deae9c05322cee9f39";
    private static String code = "d14009684fb9378696e089c40eaa73af";


    public static JSONArray getDptListByParentId(Long parentId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            req.setDeptId(parentId);
            req.setLanguage("zh_CN");
            OapiV2DepartmentListsubResponse rsp = client.execute(req, access_token);
            System.out.println(rsp.getBody());

            JSONObject bodyJson = JSON.parseObject(rsp.getBody());
            JSONArray resultJsonArray = bodyJson.getJSONArray("result");
            return resultJsonArray;
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
        }
        return new JSONArray();
    }

    private static JSONArray getDptListByLoop(JSONArray orgJsonArray, Long orgId) {
        try {
            // 查询
            JSONArray jsonArray = getDptListByParentId(orgId);
            orgJsonArray.addAll(jsonArray);
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                Long deptId = jsonObject.getLong("dept_id");
                if (deptId != null) {
                    getDptListByLoop(orgJsonArray, deptId);
                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
        }
        return orgJsonArray;
    }

    private List<ZtsOrgGroupInfo> convertDingDingGroupList(JSONArray list) {
        List<ZtsOrgGroupInfo> groupInfoList = new ArrayList<>();
        ZtsOrgGroupInfo group = null;

        for (Object object : list) {
            JSONObject jsonStr = (JSONObject) object;
            //long groupId = idGenerator.nextId(group).longValue();
            group = new ZtsOrgGroupInfo();
            //group.setGroupId(groupId);
            group.setExtraId(jsonStr.getString("dept_id"));
            group.setGroupName(jsonStr.getString("name"));
            group.setExtraParentId(jsonStr.getString("parent_id"));
            groupInfoList.add(group);
        }

        Map<String, Long> map = groupInfoList.stream().collect(Collectors.toMap(ZtsOrgGroupInfo::getExtraId, ZtsOrgGroupInfo::getGroupId));

        for (ZtsOrgGroupInfo groupInfo : groupInfoList) {
            groupInfo.setGroupPid(map.get(groupInfo.getExtraParentId()));
        }

        return groupInfoList;
    }


    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    /**
     * 获取第三方企业内部应用的access_token
     * @return
     * @throws Exception
     */
    public static String getCorpAccessToken() throws Exception {
        com.aliyun.dingtalkoauth2_1_0.Client client = createClient();
        com.aliyun.dingtalkoauth2_1_0.models.GetCorpAccessTokenRequest getCorpAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetCorpAccessTokenRequest()
                .setSuiteKey(accessKey)
                .setSuiteSecret(accessSecret)
                .setAuthCorpId(corpId)
                .setSuiteTicket(apiToken);
        try {
            GetCorpAccessTokenResponse corpAccessToken = client.getCorpAccessToken(getCorpAccessTokenRequest);
            GetCorpAccessTokenResponseBody body = corpAccessToken.getBody();
            log.info("GetCorpAccessTokenResponseBody:{}", body);
            return body.accessToken;
        } catch (TeaException err) {
            log.error(ExceptionUtil.getMessage(err));
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            log.error(ExceptionUtil.getMessage(_err));
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return null;
    }

    /**
     * {"errcode":0,"errmsg":"ok","result":{"device_id":"9f6448e2e7b12cdf333db0236f03c51d","name":"雷强强","sys":true,"sys_level":2,"unionid":"WqYZiPnkHB34iP8BhxIjzh9wiEiE","userid":"200407370437919863"},"request_id":"16kcpbwhzowzg"}
     * @throws Exception
     */

    private static String getUserByCode() throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getuserinfo");
        OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
        req.setCode(code);
        OapiV2UserGetuserinfoResponse rsp = client.execute(req, access_token);
        String body = rsp.getBody();
        JSONObject parseObject = JSON.parseObject(body);
        JSONObject result = parseObject.getJSONObject("result");
        String userid = result.getString("userid");
        System.out.println(rsp.getBody()); // 200407370437919863
        return userid;
    }

    /**
     * {"errcode":0,"errmsg":"ok","result":{"active":true,"admin":true,"avatar":"https:\/\/static-legacy.dingtalk.com\/media\/lADPDiCpsQW2jcbNAqHNAqM_675_673.jpg","boss":false,"create_time":"2024-07-15T07:05:43.000Z","dept_id_list":[1,919684607],"dept_order_list":[{"dept_id":1,"order":179997318850453160},{"dept_id":919684607,"order":176188733616559512}],"email":"","exclusive_account":false,"hide_mobile":false,"job_number":"","leader_in_dept":[{"dept_id":919684607,"leader":false},{"dept_id":1,"leader":true}],"mobile":"18668303631","real_authed":true,"remark":"","role_list":[{"group_name":"默认","id":4065312371,"name":"主管"},{"group_name":"默认","id":4065312368,"name":"主管理员"}],"senior":false,"state_code":"86","telephone":"","unionid":"WqYZiPnkHB34iP8BhxIjzh9wiEiE","userid":"200407370437919863"},"request_id":"16m9qc9gk65kv"}
     * @param userid
     * @throws Exception
     */
    private static JSONObject getUserDetailByUserid(String userid) throws Exception {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userid);
            req.setLanguage("en_US");
            OapiV2UserGetResponse rsp = client.execute(req, access_token);


            String body = rsp.getBody();
            JSONObject parseObject = JSON.parseObject(body);
            JSONObject result = parseObject.getJSONObject("result");



            System.out.println(rsp.getBody());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * {"errcode":0,"errmsg":"ok","result":{"active":true,"admin":true,"avatar":"https:\/\/static-legacy.dingtalk.com\/media\/lADPDiCpsQW2jcbNAqHNAqM_675_673.jpg","boss":false,"create_time":"2024-07-15T07:05:43.000Z","dept_id_list":[1,919684607],"dept_order_list":[{"dept_id":1,"order":179997318850453160},{"dept_id":919684607,"order":176188733616559512}],"email":"","exclusive_account":false,"hide_mobile":false,"job_number":"","leader_in_dept":[{"dept_id":919684607,"leader":false},{"dept_id":1,"leader":true}],"mobile":"18668303631","real_authed":true,"remark":"","role_list":[{"group_name":"默认","id":4065312371,"name":"主管"},{"group_name":"默认","id":4065312368,"name":"主管理员"}],"senior":false,"state_code":"86","telephone":"","unionid":"WqYZiPnkHB34iP8BhxIjzh9wiEiE","userid":"200407370437919863"},"request_id":"16m9qc9gk65kv"}
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String userid = getUserByCode();
        JSONObject userDetailByUserid = getUserDetailByUserid(userid);
        System.out.println(userDetailByUserid);
    }
}
