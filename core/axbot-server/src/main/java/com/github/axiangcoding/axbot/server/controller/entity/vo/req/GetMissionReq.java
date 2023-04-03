package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class GetMissionReq {
    @UUID
    @NotNull
    String id;
}
