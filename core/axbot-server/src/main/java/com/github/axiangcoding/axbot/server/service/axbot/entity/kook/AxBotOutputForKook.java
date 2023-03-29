package com.github.axiangcoding.axbot.server.service.axbot.entity.kook;

import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotOutputForKook extends AxBotOutput {
    // 回复的频道
    String replayToChannel;
    // 回复的消息的类型
    String replayMessageType;
}
