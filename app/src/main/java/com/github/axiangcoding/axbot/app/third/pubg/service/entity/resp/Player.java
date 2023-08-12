package com.github.axiangcoding.axbot.app.third.pubg.service.entity.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Player {
    String type;
    String id;
    Attributes attributes;
    Relationships relationships;
    Links links;

    @Getter
    @Setter
    @ToString
    public static class Attributes {
        String titleId;
        String shardId;
        String patchVersion;
        String banType;
        String clanId;
        String name;
        Object stats;
    }

    @Getter
    @Setter
    @ToString
    public static class Relationships {
        Assets assets;
        Matches matches;

        @Getter
        @Setter
        @ToString
        public static class Assets {
            Object data;
        }

        @Getter
        @Setter
        @ToString
        public static class Matches {
            Object data;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Links {
        String self;
        String schema;
    }
}
