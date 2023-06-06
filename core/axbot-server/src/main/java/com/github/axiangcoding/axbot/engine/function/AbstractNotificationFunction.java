package com.github.axiangcoding.axbot.engine.function;

import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationOutput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractNotificationFunction {
    public abstract KookNotificationOutput execute(KookNotificationInput input);
    public abstract CqhttpNotificationOutput execute(CqhttpNotificationInput input);
}
