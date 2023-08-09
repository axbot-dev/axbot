package com.github.axiangcoding.axbot.app.server.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SyncPlayerTaskConfig {
    String playerId;
    String playerName;
    String platform;
}
