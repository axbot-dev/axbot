package com.github.axiangcoding.axbot.bot.kook.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CommonUser {
    String id;
    String username;
    String nickname;
    String identifyNum;
    Boolean online;
    Integer status;
    String avatar;
    String vipAvatar;
    Boolean isVip;
    Boolean bot;
    Boolean mobileVerified;
    List<Long> roles;
    Long joinedAt;
    Long activeTime;
}
