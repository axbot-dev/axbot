package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePwdReq {
    @NotNull
    String oldPassword;
    @NotNull
    String newPassword;

}
