package com.github.axiangcoding.axbot.app.server.data.entity;


import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Column;
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
@Accessors(chain = true)
@Entity
@Table(indexes = {
        @Index(columnList = "isSensitive")
})
public class TextCensor extends BasicEntity {
    @Column(unique = true)
    String text;
    Boolean isSensitive;
}
