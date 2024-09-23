package com.lms.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParamTest {
    private String CODE;
    private String SERVICEID;
    private String SID;
    private String TIMESTAMP;
    private String LOGINACCT;
    private String NEWPASSWORD;
}
