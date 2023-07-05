package com.github.axiangcoding.axbot.app.server.controller.v2.entity.resp;


import com.github.axiangcoding.axbot.app.server.data.entity.ApiKey;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiKeyResp {
    String val;
    String description;
    Boolean neverExpire;
    LocalDateTime expireTime;

    public static ApiKeyResp from(ApiKey apiKey) {
        ApiKeyResp vo = new ApiKeyResp();
        vo.setVal(apiKey.getVal());
        vo.setDescription(apiKey.getDescription());
        vo.setNeverExpire(apiKey.getNeverExpire());
        vo.setExpireTime(apiKey.getExpireTime());
        return vo;
    }
}
