package com.github.axiangcoding.axbot.engine.entity.cqhttp;

import com.github.axiangcoding.axbot.engine.entity.AxBotUserInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotUserInputForCqhttp extends AxBotUserInput {
    String fromGroup;
}
