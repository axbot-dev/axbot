package com.github.axiangcoding.axbot.engine.function.notification;

import com.github.axiangcoding.axbot.engine.NotificationEvent;
import com.github.axiangcoding.axbot.engine.annot.AxbotNotificationFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractNotificationFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.server.service.QGroupSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@AxbotNotificationFunc(event = NotificationEvent.EXIT_GUILD)
@Component
public class FuncExitGuild extends AbstractNotificationFunction {
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
