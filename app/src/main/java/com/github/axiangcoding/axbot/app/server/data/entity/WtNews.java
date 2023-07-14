package com.github.axiangcoding.axbot.app.server.data.entity;


import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Table
@Accessors(chain = true)
public class WtNews extends BasicEntity {
    @Column(unique = true)
    String url;

    String title;

    String posterUrl;
    @Column(columnDefinition = "TEXT")
    String comment;
    String dateStr;
}
