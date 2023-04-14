package com.github.axiangcoding.axbot.server.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * implements gson
 * TODO: 序列号空值的LocalDateTime时会报错
 */
public class JsonUtils {
    private static final Gson GSON = new Gson();
    private static final Gson GSON_LOW_CASE_UNDER_SCORES = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromLowCaseUnderscoresJson(String json, Class<T> cassOfT) {
        return GSON_LOW_CASE_UNDER_SCORES.fromJson(json, cassOfT);
    }

    public static String toLowCaseUnderscoresJson(Object object) {
        return GSON_LOW_CASE_UNDER_SCORES.toJson(object);
    }
}
