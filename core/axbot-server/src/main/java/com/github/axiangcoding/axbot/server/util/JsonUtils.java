package com.github.axiangcoding.axbot.server.util;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;

/**
 * implements gson
 * TODO: 序列号空值的LocalDateTime时会报错
 */
public class JsonUtils {

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return JSONObject.parseObject(json, classOfT);
    }

    public static String toJson(Object object) {
        return JSONObject.toJSONString(object);
    }

    public static <T> T fromLowCaseUnderscoresJson(String json, Class<T> cassOfT) {
        return JSONObject.parseObject(json, cassOfT, JSONReader.Feature.SupportSmartMatch);
    }

}
