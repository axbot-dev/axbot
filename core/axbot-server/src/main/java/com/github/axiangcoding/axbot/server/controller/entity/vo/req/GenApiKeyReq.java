package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenApiKeyReq {
    @Nullable
    String comment;
    @NotNull
    Boolean neverExpire;
    Long expireInSecond;
}
