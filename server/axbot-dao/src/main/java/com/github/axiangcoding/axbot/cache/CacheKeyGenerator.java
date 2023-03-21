package com.github.axiangcoding.axbot.cache;

public class CacheKeyGenerator {
    private static final String GAMER_PROFILE_UPDATE_CACHE_PREFIX = "WTGamerProfileUpdate";

    public static String getWtGamerProfileUpdateCacheKey(String nickname) {
        return "%s:%s".formatted(GAMER_PROFILE_UPDATE_CACHE_PREFIX, nickname);
    }
}
