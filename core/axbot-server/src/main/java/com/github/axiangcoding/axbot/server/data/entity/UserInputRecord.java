package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = {
        @Index(columnList = "platform"),
        @Index(columnList = "sensitive"),
        @Index(columnList = "userId")})

public class UserInputRecord extends BasicEntity {
    String userId;
    String platform;
    String input;
    String fromKookGuild;
    String fromKookChannel;
    String fromQGroup;
    Boolean sensitive;
}
