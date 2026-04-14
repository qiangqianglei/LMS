package com.lms.service.impl;

import com.lms.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * POST请求 - 发送JSON数据
     */
    public ResponseEntity<String> postJson(String url, Object requestBody) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 如有需要，添加认证头
        // headers.set("Authorization", "Bearer token");

        // 创建请求实体
        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

        try {
            // 发送POST请求
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url,
                    request,
                    String.class
            );

            log.info("POST请求成功，URL: {}, 状态码: {}", url, response.getStatusCode());
            return response;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP错误，URL: {}, 状态码: {}, 响应: {}",
                    url, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw e;
        } catch (ResourceAccessException e) {
            log.error("网络连接错误，URL: {}", url, e);
            throw e;
        } catch (Exception e) {
            log.error("未知错误，URL: {}", url, e);
            throw e;
        }
    }

    /**
     * POST请求 - 发送表单数据
     */
    public ResponseEntity<String> postForm(String url, Map<String, String> formData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(formData, headers);

        return restTemplate.postForEntity(url, request, String.class);
    }

    /**
     * 带自定义返回类型的POST请求
     */
    public <T> T postForObject(String url, Object requestBody, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(url, request, responseType);
    }
}
