package com.github.axiangcoding.axbot.server.service.axbot;

import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotSysOutput;

public interface SystemInputCallback {
    void callback(AxBotSysOutput output);
}
