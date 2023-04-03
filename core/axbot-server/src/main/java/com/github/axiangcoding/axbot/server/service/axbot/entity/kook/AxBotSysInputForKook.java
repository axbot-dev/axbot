package com.github.axiangcoding.axbot.server.service.axbot.entity.kook;

import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotSysInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotSysInputForKook extends AxBotSysInput {
    /**
     * 请求来自的服务器
     */
    String fromGuild;
}
