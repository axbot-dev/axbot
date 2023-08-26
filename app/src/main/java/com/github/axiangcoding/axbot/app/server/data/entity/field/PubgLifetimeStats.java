package com.github.axiangcoding.axbot.app.server.data.entity.field;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Embeddable
@Accessors(chain = true)
public class PubgLifetimeStats {
    PubgGameStats duo;
    PubgGameStats duoFpp;
    PubgGameStats solo;
    PubgGameStats soloFpp;
    PubgGameStats squad;
    PubgGameStats squadFpp;

}
