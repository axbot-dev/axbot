package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import lombok.Data;

@Data
public class QQWebhookEvent {
    Long time;
    Long selfId;
    String postType;
    String messageType;
    String requestType;
    String noticeType;
    String metaEventType;
    String subType;
    Long messageId;
    Long userId;
    Long groupId;
    Object message;
    String rawMessage;
    Integer font;
    Sender sender;

    @Data
    public static class Sender {
        Integer age;
        String area;
        String card;
        String level;
        String nickname;
        String role;
        String sex;
        String title;
        Long userId;
    }

}
