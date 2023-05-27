package com.github.axiangcoding.axbot.remote.kook.service.entity.req;

import lombok.Data;


@Data
public class CreateDirectMsgReq {
    Integer type;
    String targetId;
    String chatCode;
    String content;
    String quote;
    String nonce;
}
