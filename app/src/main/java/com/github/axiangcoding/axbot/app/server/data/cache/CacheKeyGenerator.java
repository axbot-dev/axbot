package com.github.axiangcoding.axbot.app.server.data.cache;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;

public class CacheKeyGenerator {

    private enum CACHE_PREFIX {
        GAMER_PROFILE_UPDATE,
        BILI_ROOM_REMIND,
        USER_LUCK,
    }

    public static String getWtGamerProfileUpdateCacheKey(String nickname) {
        return "%s:%s".formatted(CACHE_PREFIX.GAMER_PROFILE_UPDATE.name(), nickname);
    }

    public static String getKookBiliRoomRemindCacheKey(String channelId, String roomId) {
        return "%s:kook:%s:%s".formatted(CACHE_PREFIX.BILI_ROOM_REMIND.name(), channelId, roomId);
    }

    public static String getCqhttpBiliRoomRemindCacheKey(String groupId, String roomId) {
        return "%s:cqhttp:%s:%s".formatted(CACHE_PREFIX.BILI_ROOM_REMIND.name(), groupId, roomId);
    }

    public static String getUserLuckKey(BotPlatform platform, String userId) {
        return "%s:%s:%s".formatted(CACHE_PREFIX.USER_LUCK.name(), platform.name(), userId);
    }
}
