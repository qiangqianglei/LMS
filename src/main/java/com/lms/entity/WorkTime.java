package com.lms.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkTime {
    private Integer id;
    private String date;
    private String extraWorkHour;
    private String workHour;
    private String userId;
}
