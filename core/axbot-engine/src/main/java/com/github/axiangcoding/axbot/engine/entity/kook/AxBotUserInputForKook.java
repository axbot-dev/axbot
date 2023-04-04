package com.github.axiangcoding.axbot.engine.entity.kook;

import com.github.axiangcoding.axbot.engine.entity.AxBotUserInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotUserInputForKook extends AxBotUserInput {
    /**
     * 请求来自的频道
     */
    String fromChannel;
    /**
     * 请求来自的服务器
     */
    String fromGuild;
}
