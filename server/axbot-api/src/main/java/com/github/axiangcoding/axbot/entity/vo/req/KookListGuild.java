package com.github.axiangcoding.axbot.entity.vo.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class KookListGuild {
    @Min(1)
    @NotNull
    Integer page;
    @Min(1)
    @Max(100)
    @NotNull
    Integer pageSize;

    String sort;
}
