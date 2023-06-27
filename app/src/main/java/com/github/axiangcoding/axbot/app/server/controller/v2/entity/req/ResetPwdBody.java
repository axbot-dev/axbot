package com.github.axiangcoding.axbot.app.server.controller.v2.entity.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPwdBody {
    @NotBlank
    @Length(min = 4, max = 16, message = "old password must be 4-16 characters")
    String oldPassword;
    @NotBlank
    @Length(min = 4, max = 16, message = "new password must be 4-16 characters")
    String newPassword;

}
