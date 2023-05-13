package com.github.axiangcoding.axbot.engine.v1.io.cqhttp;

import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CqhttpInteractiveInput extends InteractiveInput {
    String groupId;

    @Override
    public CqhttpInteractiveOutput response(String response) {
        CqhttpInteractiveOutput output = new CqhttpInteractiveOutput();
        output.setUserId(this.getUserId());
        output.setMessageId(this.getMessageId());
        output.setResponse(response);
        output.setTimeUsage(Duration.between(getStartTime(), LocalDateTime.now()));
        output.setGroupId(groupId);
        return output;
    }
}
