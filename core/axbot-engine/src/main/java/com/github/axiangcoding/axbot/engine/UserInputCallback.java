package com.github.axiangcoding.axbot.engine;


import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;

public interface UserInputCallback {
    void callback(AxBotUserOutput output);
    void catchException(Exception e);
}
