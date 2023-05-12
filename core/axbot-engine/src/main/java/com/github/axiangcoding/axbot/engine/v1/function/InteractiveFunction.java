package com.github.axiangcoding.axbot.engine.v1.function;

import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class InteractiveFunction {
    public abstract KookInteractiveOutput execute(KookInteractiveInput input);
    public abstract CqhttpInteractiveOutput execute(CqhttpInteractiveInput input);
}
