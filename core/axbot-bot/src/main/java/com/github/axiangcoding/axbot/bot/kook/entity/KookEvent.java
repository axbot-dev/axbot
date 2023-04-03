package com.github.axiangcoding.axbot.bot.kook.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * introduce at <a href="https://developer.kookapp.cn/doc/event/event-introduction">event-introduction</a>
 */
@Data
public class KookEvent {
    // TODO 更多的定义
    public static final Integer TYPE_MESSAGE = 255;
    public static final Integer TYPE_TEXT = 1;
    public static final Integer TYPE_PIC = 2;
    public static final Integer TYPE_VIDEO = 3;
    public static final Integer TYPE_KMARKDOWN = 9;
    public static final Integer TYPE_CARD = 10;

    public static final String CHANNEL_TYPE_WEBHOOK_CHALLENGE = "WEBHOOK_CHALLENGE";
    public static final String CHANNEL_TYPE_GROUP = "GROUP";
    public static final String CHANNEL_TYPE_PERSON = "PERSON";
    public static final String CHANNEL_TYPE_BROADCAST = "BROADCAST";

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
    Extra extra;

    @Getter
    @Setter
    @ToString
    public static class Extra {
        String type;
        String guildId;
        String channelName;
        // List<Mention> mention;
        Boolean mentionAll;
        // List<MentionRole> mentionRoles;
        Boolean mentionHere;
        Map<String, Object> author;
        Map<String, Object> body;
    }
}
