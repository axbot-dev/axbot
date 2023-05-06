package com.github.axiangcoding.axbot.server.controller.entity.vo.resp;

import com.github.axiangcoding.axbot.server.data.entity.ApiKey;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiKeyVo {
    String key;
    String comment;
    Boolean neverExpire;
    LocalDateTime expireTime;

    public static ApiKeyVo from(ApiKey apiKey) {
        ApiKeyVo vo = new ApiKeyVo();
        vo.setKey(apiKey.getKey());
        vo.setComment(apiKey.getComment());
        vo.setNeverExpire(apiKey.getNeverExpire());
        vo.setExpireTime(apiKey.getExpireTime());
        return vo;
    }
}
