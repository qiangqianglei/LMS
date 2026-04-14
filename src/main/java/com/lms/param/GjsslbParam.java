package com.lms.param;

import lombok.Data;

import java.util.Date;

@Data
public class GjsslbParam {
    // 案件类型
    private String ajlx;

    // 页码
    private Integer pageNum = 1;

    // 搜索关键词
    private String search;

    // 发布时间开始
    private Date fbkssj;

    // 发布时间结束
    private Date fbjssj;

    // 发布人
    private String fbr;

    // 发布人类型
    private String fbrlx;

    // 内容类别
    private String nrlb;
}
