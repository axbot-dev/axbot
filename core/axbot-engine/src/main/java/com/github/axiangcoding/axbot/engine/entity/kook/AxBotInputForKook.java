package com.github.axiangcoding.axbot.engine.entity.kook;

import com.github.axiangcoding.axbot.engine.entity.AxBotInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotInputForKook extends AxBotInput {
    String requestChannel;
}
