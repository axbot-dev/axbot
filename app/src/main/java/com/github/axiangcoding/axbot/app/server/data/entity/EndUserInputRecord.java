package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
public class EndUserInputRecord extends BasicEntity {
    String userId;
    String platform;
    String input;
    String command;
    String guildId;
    String channelId;
    Boolean isSensitive;
}
