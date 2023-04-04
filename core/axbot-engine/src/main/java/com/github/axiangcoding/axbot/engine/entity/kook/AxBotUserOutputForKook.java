package com.github.axiangcoding.axbot.engine.entity.kook;

import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotUserOutputForKook extends AxBotUserOutput {
    // 回复的频道
    String toChannel;
    // 回复的消息的类型
    String replayMessageType;
}
