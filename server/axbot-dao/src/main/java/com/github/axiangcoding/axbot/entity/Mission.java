package com.github.axiangcoding.axbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = {
})
public class Mission extends BasicEntity {
    public static final String STATUS_UNKNOWN = "unknown";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILED = "failed";

    public static final String TYPE_CRAWLER = "crawler";

    @Column(unique = true)
    UUID missionId;

    String type;
    String status;
    LocalDateTime beginTime;
    LocalDateTime finishTime;
    Double process;

    @Column(columnDefinition = "TEXT")
    String info;

    @Column(columnDefinition = "TEXT")
    String result;

    public Mission() {
        super();
        this.missionId = UUID.randomUUID();
        this.status = STATUS_UNKNOWN;
    }

}
