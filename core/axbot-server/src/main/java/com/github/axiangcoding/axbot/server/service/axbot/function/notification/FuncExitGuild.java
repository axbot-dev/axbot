package com.github.axiangcoding.axbot.server.service.axbot.function.notification;

import com.github.axiangcoding.axbot.engine.v1.function.NotificationFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.server.service.QGroupSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class FuncExitGuild extends NotificationFunction {
    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    QGroupSettingService qGroupSettingService;

    @Override
    public KookNotificationOutput execute(KookNotificationInput input) {
        kookGuildSettingService.updateWhenExit(input.getGuildId());
        return null;
    }

    @Override
    public CqhttpNotificationOutput execute(CqhttpNotificationInput input) {
        qGroupSettingService.updateWhenExit(input.getGroupId());
        return null;
    }
}
