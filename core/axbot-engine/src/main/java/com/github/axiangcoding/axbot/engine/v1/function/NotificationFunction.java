package com.github.axiangcoding.axbot.engine.v1.function;

import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationOutput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class NotificationFunction {
    public abstract KookNotificationOutput execute(KookNotificationInput input);
    public abstract CqhttpNotificationOutput execute(CqhttpNotificationInput input);
}