package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GlobalSetting {
    @Length(max = 255)
    @NotBlank
    String key;
    @NotBlank
    String value;
    @Length(max = 255)
    String remark;
}
