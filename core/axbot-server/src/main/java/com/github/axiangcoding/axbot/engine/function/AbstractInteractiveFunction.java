package com.github.axiangcoding.axbot.engine.function;

import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractInteractiveFunction {
    public abstract KookInteractiveOutput execute(KookInteractiveInput input);
    public abstract CqhttpInteractiveOutput execute(CqhttpInteractiveInput input);
}
