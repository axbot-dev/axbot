package com.github.axiangcoding.axbot.engine.entity.kook;

import com.github.axiangcoding.axbot.engine.entity.AxBotSysInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotSysInputForKook extends AxBotSysInput {
    /**
     * 需要发送给的服务器
     */
    String fromGuild;
}
