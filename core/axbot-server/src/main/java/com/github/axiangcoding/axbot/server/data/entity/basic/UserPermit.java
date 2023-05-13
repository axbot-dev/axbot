package com.github.axiangcoding.axbot.server.data.entity.basic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Embeddable
public class UserPermit {
    Boolean canUseAIChat;

    public static UserPermit defaultPermit() {
        UserPermit permit = new UserPermit();
        permit.setCanUseAIChat(false);
        return permit;
    }
}
