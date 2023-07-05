package com.github.axiangcoding.axbot.app.server.controller.v2.entity.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BotInfoResp {
    String component;
    String id;
    String username;
    Boolean isActive;
}
