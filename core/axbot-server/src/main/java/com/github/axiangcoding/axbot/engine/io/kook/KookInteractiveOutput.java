package com.github.axiangcoding.axbot.engine.io.kook;

import com.github.axiangcoding.axbot.engine.io.InteractiveOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KookInteractiveOutput extends InteractiveOutput {
    String guildId;
    String channelId;
    Boolean temp;
}
