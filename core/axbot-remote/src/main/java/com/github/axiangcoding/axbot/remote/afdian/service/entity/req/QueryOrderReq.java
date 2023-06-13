package com.github.axiangcoding.axbot.remote.afdian.service.entity.req;

import lombok.Data;

@Data
public class QueryOrderReq {
    String userId;
    String params;
    Long ts;
    String sign;
}
