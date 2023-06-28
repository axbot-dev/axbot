package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndUserStatus;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndUserUsage;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.SponsorPlan;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = {
        @Index(columnList = "userId, platform", unique = true)
})
@Accessors(chain = true)
public class EndUser extends BasicEntity {
    String userId;

    String platform;

    Boolean isSuperAdmin;

    @Embedded
    EndUserStatus status;

    @Embedded
    EndUserUsage usage;

    @Embedded
    SponsorPlan sponsor;

    public static EndUser init(String userId, String platform) {
        return new EndUser()
                .setUserId(userId)
                .setPlatform(platform)
                .setIsSuperAdmin(false)
                .setStatus(EndUserStatus.normal())
                .setUsage(EndUserUsage.init())
                .setSponsor(new SponsorPlan());
    }
}
