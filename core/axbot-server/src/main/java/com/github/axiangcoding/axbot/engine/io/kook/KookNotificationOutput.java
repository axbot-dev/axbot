package com.github.axiangcoding.axbot.engine.io.kook;

import com.github.axiangcoding.axbot.engine.io.NotificationOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KookNotificationOutput extends NotificationOutput {
    String guildId;
    String channelId;
}
