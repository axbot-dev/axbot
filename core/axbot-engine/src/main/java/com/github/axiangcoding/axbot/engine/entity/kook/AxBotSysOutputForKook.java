package com.github.axiangcoding.axbot.engine.entity.kook;

import com.github.axiangcoding.axbot.engine.entity.AxBotSysOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotSysOutputForKook extends AxBotSysOutput {
    /**
     * 回复的频道
     */
    String toChannel;
    /**
     * 消息的类型
     */
    String contentType;
}
