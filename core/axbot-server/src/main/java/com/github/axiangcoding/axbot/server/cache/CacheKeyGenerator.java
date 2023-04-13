package com.github.axiangcoding.axbot.server.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CacheKeyGenerator {
    @AllArgsConstructor
    @Getter
    private enum CACHE_PREFIX {
        GAMER_PROFILE_UPDATE("WTGamerProfileUpdate"),
        BILI_ROOM_REMIND("BiliBiliRoomRemind"),
        ;
        private final String key;
    }

    public static String getWtGamerProfileUpdateCacheKey(String nickname) {
        return "%s:%s".formatted(CACHE_PREFIX.GAMER_PROFILE_UPDATE.getKey(), nickname);
    }

    public static String getBiliRoomRemindCacheKey(String channelId, String roomId) {
        return "%s:%s:%s".formatted(CACHE_PREFIX.BILI_ROOM_REMIND, channelId, roomId);
    }
}
