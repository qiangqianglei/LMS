package com.lms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.entity.WorkTime;
import com.lms.mapper.WorkTimeMapper;
import com.lms.service.WorkTimeService;
import com.lms.vo.SysResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkTimeServiceImpl extends ServiceImpl<WorkTimeMapper, WorkTime> implements WorkTimeService {
    @Override
    public SysResult saveWorkTime(WorkTime workTime) {
        this.save(workTime);
        return SysResult.success();
    }

    @Override
    public SysResult updateWorkTime(WorkTime workTime) {
        this.updateById(workTime);
        return SysResult.success();
    }

    @Override
    public SysResult deleteWorkTime(Integer id) {
        this.removeById(id);
        return SysResult.success();
    }

    @Override
    public SysResult pageWorkTime() {
        List<WorkTime> list = this.list();
        return SysResult.success(list);
    }
}
