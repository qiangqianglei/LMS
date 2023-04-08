package com.lms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan(basePackages = "com.lms.mapper")
public class LMSApplication {
    public static void main(String[] args) {
        SpringApplication.run(LMSApplication.class, args);
    }
}
