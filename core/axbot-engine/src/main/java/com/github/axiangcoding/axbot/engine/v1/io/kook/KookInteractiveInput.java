package com.github.axiangcoding.axbot.engine.v1.io.kook;

import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class KookInteractiveInput extends InteractiveInput {
    String guildId;
    String channelId;

    @Override
    public KookInteractiveOutput response(String response) {
        KookInteractiveOutput output = new KookInteractiveOutput();
        output.setUserId(getUserId());
        output.setMessageId(getMessageId());
        output.setResponse(response);
        output.setTimeUsage(Duration.between(getStartTime(), LocalDateTime.now()));
        output.setGuildId(guildId);
        output.setChannelId(channelId);
        return output;
    }
}
