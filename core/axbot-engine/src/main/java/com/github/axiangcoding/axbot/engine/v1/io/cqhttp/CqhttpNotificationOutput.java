package com.github.axiangcoding.axbot.engine.v1.io.cqhttp;

import com.github.axiangcoding.axbot.engine.v1.io.NotificationOutput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CqhttpNotificationOutput extends NotificationOutput {
    String groupId;
}
