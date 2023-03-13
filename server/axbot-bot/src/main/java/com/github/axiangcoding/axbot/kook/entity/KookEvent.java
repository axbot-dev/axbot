package com.github.axiangcoding.axbot.kook.entity;

import lombok.Data;

/**
 * introduce at <a href="https://developer.kookapp.cn/doc/event/event-introduction">event-introduction</a>
 */
@Data
public class KookEvent {
    String channelType;
    String challenge;
    String verifyToken;
    Integer type;
    String targetId;
    String authorId;
    String content;
    String msgId;
    Long msgTimestamp;
    String nonce;
    Object extra;
}
