package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireApiKey;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.ResetLockReq;
import com.github.axiangcoding.axbot.server.schedule.ScheduleTask;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("v1/system")
public class SystemController {
    @Resource
    ScheduleTask scheduleTask;

    @GetMapping("version")
    public CommonResult getVersion() {
        return CommonResult.success("version", System.getenv("APP_VERSION"));
    }

    @GetMapping("health")
    public CommonResult health() {
        return CommonResult.success();
    }

    @RequireApiKey(admin = true)
    @PostMapping("task/lock/reset")
    public CommonResult resetLock(@Valid @ParameterObject ResetLockReq req) {
        scheduleTask.resetLock(req.getLock());
        return CommonResult.success();
    }
}
