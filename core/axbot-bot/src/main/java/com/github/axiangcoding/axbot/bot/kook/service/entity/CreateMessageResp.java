package com.github.axiangcoding.axbot.bot.kook.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateMessageResp extends CommonResp {
    Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        String msgId;
        String msgTimestamp;
        String nonce;
    }
}
