package com.github.axiangcoding.axbot.engine.v1.io.cqhttp;

import com.github.axiangcoding.axbot.engine.v1.io.NotificationInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CqhttpNotificationInput extends NotificationInput {
    String groupId;

    @Override
    public CqhttpNotificationOutput response(String response) {
        CqhttpNotificationOutput output = new CqhttpNotificationOutput();
        output.setGroupId(this.getGroupId());
        output.setResponse(response);
        return output;
    }
}
