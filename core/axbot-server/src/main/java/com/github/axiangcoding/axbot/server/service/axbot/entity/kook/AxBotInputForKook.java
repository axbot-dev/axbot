package com.github.axiangcoding.axbot.server.service.axbot.entity.kook;

import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotInputForKook extends AxBotInput {
    /**
     * 请求来自的频道
     */
    String requestChannel;
    /**
     * 请求来自的服务器
     */
    String requestGuild;
}
