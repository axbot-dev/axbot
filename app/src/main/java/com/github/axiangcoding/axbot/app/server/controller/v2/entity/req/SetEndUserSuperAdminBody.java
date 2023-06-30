package com.github.axiangcoding.axbot.app.server.controller.v2.entity.req;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetEndUserSuperAdminBody {
    @NotNull
    BotPlatform platform;
    @NotBlank
    String userId;
    @NotNull
    Boolean isSuperAdmin;
}
