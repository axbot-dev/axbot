package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import com.github.axiangcoding.axbot.server.schedule.ScheduleTask;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetLockReq {
    @NotNull
    ScheduleTask.LOCK lock;
}
