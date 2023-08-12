package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
public class Task extends BasicEntity {
    public enum TYPE {
        SYNC_PLAYER,
    }

    public enum STATUS {
        PENDING,
        RUNNING,
        FINISHED,
        FAILED
    }

    @Column(unique = true)
    String taskId;
    String type;
    String status;
    Double progress;
    @Column(columnDefinition = "json")
    String config;
    @Column(columnDefinition = "TEXT")
    String result;

    public static Task initSyncPlayer(String config) {
        return new Task().setTaskId(UUID.randomUUID().toString())
                .setStatus(STATUS.PENDING.name())
                .setType(TYPE.SYNC_PLAYER.name())
                .setProgress(0.0)
                .setConfig(config);
    }
}
