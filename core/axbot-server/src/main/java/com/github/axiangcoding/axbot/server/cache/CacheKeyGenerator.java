package com.github.axiangcoding.axbot.server.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CacheKeyGenerator {
    @AllArgsConstructor
    @Getter
    private enum CACHE_PREFIX {
        GAMER_PROFILE_UPDATE("WTGamerProfileUpdate"),
        BILI_ROOM_REMIND("BiliBiliRoomRemind"),
        CRON_JOB_LOCK("CronJobLock");
        private final String key;
    }

    public static String getWtGamerProfileUpdateCacheKey(String nickname) {
        return "%s:%s".formatted(CACHE_PREFIX.GAMER_PROFILE_UPDATE.getKey(), nickname);
    }

    public static String getKookBiliRoomRemindCacheKey(String channelId, String roomId) {
        return "%s:kook:%s:%s".formatted(CACHE_PREFIX.BILI_ROOM_REMIND.getKey(), channelId, roomId);
    }

    public static String getCqhttpBiliRoomRemindCacheKey(String groupId, String roomId) {
        return "%s:cqhttp:%s:%s".formatted(CACHE_PREFIX.BILI_ROOM_REMIND.getKey(), groupId, roomId);
    }

    public static String getCronJobLockKey(String jobName) {
        return "%s:%s".formatted(CACHE_PREFIX.CRON_JOB_LOCK.getKey(), jobName);
    }
}
