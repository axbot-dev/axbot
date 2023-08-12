package com.github.axiangcoding.axbot.app.server.data.cache;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;

public class CacheKeyGenerator {

    private enum CACHE_PREFIX {
        /**
         * PUBG玩家同步冻结
         */
        PUBG_PLAYER_SYNC_FROZEN,
        BILI_ROOM_REMIND,
        /**
         * 用户今日气运
         */
        USER_LUCK_TODAY,
    }


    public static String getKookBiliRoomRemindCacheKey(String channelId, String roomId) {
        return "%s:kook:%s:%s".formatted(CACHE_PREFIX.BILI_ROOM_REMIND.name(), channelId, roomId);
    }

    public static String getCqhttpBiliRoomRemindCacheKey(String groupId, String roomId) {
        return "%s:cqhttp:%s:%s".formatted(CACHE_PREFIX.BILI_ROOM_REMIND.name(), groupId, roomId);
    }

    public static String getUserLuckKey(BotPlatform platform, String userId) {
        return "%s:%s:%s".formatted(CACHE_PREFIX.USER_LUCK_TODAY.name(), platform.name(), userId);
    }

    public static String getPubgPlayerSyncFrozenKey(String platform, String playerId) {
        return "%s:%s:%s".formatted(CACHE_PREFIX.PUBG_PLAYER_SYNC_FROZEN.name(), platform, playerId);
    }
}
