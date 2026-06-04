package com.lms.demo.algorithm.sort;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

public class Demo31 {

    public static void main(String[] args) {
        Double score = 1779418731237D;
        if (score != null) {
            long currentTimeMillis = System.currentTimeMillis();
             if (score > System.currentTimeMillis()) {
                long betweenMin = DateUtil.between(DateUtil.date(score.longValue()), DateUtil.date(currentTimeMillis), DateUnit.MINUTE);
                 System.out.println(("该IP已被锁定，剩余：" + (betweenMin + 1) + "分钟"));
             }
        }
    }
}
