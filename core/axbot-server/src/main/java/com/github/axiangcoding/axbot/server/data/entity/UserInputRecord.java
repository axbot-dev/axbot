package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class UserInputRecord extends BasicEntity {
    String userId;
    String platform;
    String input;
    String fromKookGuild;
    String fromKookChannel;
    String fromQGroup;
}
