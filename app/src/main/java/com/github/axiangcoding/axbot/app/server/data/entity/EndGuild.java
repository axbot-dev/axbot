package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndGuildStatus;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndGuildUsage;
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
        @Index(columnList = "guildId, platform", unique = true)
})
@Accessors(chain = true)
public class EndGuild extends BasicEntity {
    String guildId;

    String platform;

    @Embedded
    EndGuildStatus status;

    @Embedded
    EndGuildUsage usage;

    @Embedded
    SponsorPlan sponsor;

    public static EndGuild init(String userId, String platform) {
        return new EndGuild()
                .setGuildId(userId)
                .setPlatform(platform)
                .setStatus(EndGuildStatus.normal())
                .setUsage(EndGuildUsage.init())
                .setSponsor(new SponsorPlan());
    }
}
