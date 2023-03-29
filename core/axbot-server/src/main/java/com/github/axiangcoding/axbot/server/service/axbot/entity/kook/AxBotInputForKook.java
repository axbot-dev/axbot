package com.github.axiangcoding.axbot.server.service.axbot.entity.kook;

import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotInputForKook extends AxBotInput {
    String requestChannel;
}
