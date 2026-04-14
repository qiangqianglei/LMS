package com.lms.service;

import org.springframework.http.ResponseEntity;

public interface ApiService {
    ResponseEntity<String> postJson(String url, Object requestBody);
}
