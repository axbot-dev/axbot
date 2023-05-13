package com.github.axiangcoding.axbot.server.data.entity.basic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Embeddable
public class UserSubscribe {
    String plan;
    LocalDateTime expireAt;
}
