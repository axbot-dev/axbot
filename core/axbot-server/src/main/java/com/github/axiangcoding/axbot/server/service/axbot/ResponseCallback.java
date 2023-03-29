package com.github.axiangcoding.axbot.server.service.axbot;

import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotOutput;

public interface ResponseCallback {
    void callback(AxBotOutput output);
}
