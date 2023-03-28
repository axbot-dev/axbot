package com.github.axiangcoding.axbot.server.entity.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetOrUpdateGamerProfile {
    @NotBlank
    String nickname;
}
