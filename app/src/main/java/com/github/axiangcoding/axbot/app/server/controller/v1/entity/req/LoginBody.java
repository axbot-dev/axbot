package com.github.axiangcoding.axbot.app.server.controller.v1.entity.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginBody {
    /**
     * 用户名
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,16}$", message = "username must be 4-16 characters, and only contains letters, numbers and underscores")
    String username;
    /**
     * 密码
     */
    @NotBlank
    @Length(min = 4, max = 16, message = "password must be 4-16 characters")
    String password;
}
