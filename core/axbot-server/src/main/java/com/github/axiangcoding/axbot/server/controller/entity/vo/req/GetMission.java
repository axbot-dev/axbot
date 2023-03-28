package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class GetMission {
    @UUID
    String id;
}
