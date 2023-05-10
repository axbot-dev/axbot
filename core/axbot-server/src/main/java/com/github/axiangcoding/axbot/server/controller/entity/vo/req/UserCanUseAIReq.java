package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCanUseAIReq {
    @NotBlank
    String userId;
    Boolean canUseAI;
}
