package com.github.axiangcoding.axbot.engine.io.cqhttp;

import com.github.axiangcoding.axbot.engine.io.InteractiveOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CqhttpInteractiveOutput extends InteractiveOutput {
    String groupId;
}
