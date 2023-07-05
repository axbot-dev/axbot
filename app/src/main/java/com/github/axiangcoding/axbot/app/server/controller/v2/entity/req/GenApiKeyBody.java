package com.github.axiangcoding.axbot.app.server.controller.v2.entity.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GenApiKeyBody {
    @Length(max = 255)
    String comment;
    @NotNull
    Boolean neverExpire;
    @PositiveOrZero
    Long expireInSecond;
}
