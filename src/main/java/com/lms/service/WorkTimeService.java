package com.lms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.entity.WorkTime;
import com.lms.vo.SysResult;

public interface WorkTimeService extends IService<WorkTime> {

    SysResult saveWorkTime(WorkTime workTime);
    SysResult updateWorkTime(WorkTime workTime);
    SysResult deleteWorkTime(Integer id);
    SysResult pageWorkTime(WorkTime workTime);
}
