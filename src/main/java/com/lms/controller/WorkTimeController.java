package com.lms.controller;

import com.lms.entity.WorkTime;
import com.lms.service.WorkTimeService;
import com.lms.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workTime")
public class WorkTimeController {

    @Autowired
    private WorkTimeService workTimeService;

    @PostMapping("/save")
    public SysResult save(@RequestBody WorkTime workTime) {
        workTimeService.save(workTime);
        return SysResult.success();
    }

    @GetMapping("/page")
    public SysResult list() {
        return workTimeService.pageWorkTime();
    }
}
