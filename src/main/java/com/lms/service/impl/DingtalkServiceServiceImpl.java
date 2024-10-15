package com.lms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubidRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubidResponse;
import com.lms.entity.ZtsOrgGroupInfo;
import com.lms.service.DingtalkService;
import com.lms.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DingtalkServiceServiceImpl implements DingtalkService {

    private static final String accessKey = "dingerbu4wwg070r2jpu";
    private static final String accessSecret = "E6i_Us19kLI29Kv97hWqcnB_U0tA865qHCclxUp6eQdkgROO9o7-m4u2oZF0m_fW";



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

    private static String access_token = "af239f68bba93977b10088517a54ef6f";


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


    public static void main(String[] args) {
        JSONArray jsonArray = getDptListByLoop(new JSONArray(), 1L);
        System.out.println(jsonArray);
    }
}
