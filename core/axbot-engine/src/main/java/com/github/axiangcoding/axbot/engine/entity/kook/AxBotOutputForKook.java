package com.github.axiangcoding.axbot.engine.entity.kook;

import com.github.axiangcoding.axbot.engine.entity.AxBotOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotOutputForKook extends AxBotOutput {
    // 回复的频道
    String replayToChannel;
}
