package com.github.axiangcoding.axbot.engine;

import com.github.axiangcoding.axbot.engine.entity.AxBotOutput;

public interface ResponseCallback {
    void callback(AxBotOutput output);
}
