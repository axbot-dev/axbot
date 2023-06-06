package com.github.axiangcoding.axbot.engine.io.kook;

import com.github.axiangcoding.axbot.engine.io.NotificationInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KookNotificationInput extends NotificationInput {
    String guildId;
    String channelId;

    @Override
    public KookNotificationOutput response(String response) {
        KookNotificationOutput output = new KookNotificationOutput();
        output.setResponse(response);
        output.setGuildId(this.getGuildId());
        output.setChannelId(this.getChannelId());
        return output;
    }
}
