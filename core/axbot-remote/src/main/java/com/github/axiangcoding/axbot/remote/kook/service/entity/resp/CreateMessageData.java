package com.github.axiangcoding.axbot.remote.kook.service.entity.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateMessageData {
    String msgId;
    String msgTimestamp;
    String nonce;
}
