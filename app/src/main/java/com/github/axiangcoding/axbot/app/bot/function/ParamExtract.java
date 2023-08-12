package com.github.axiangcoding.axbot.app.bot.function;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParamExtract {
    private final Map<String, String> paramMap;
    private final String[] paramSplit;

    public ParamExtract(String plainInput) {
        this.paramMap = getParamMap(plainInput);
        this.paramSplit = StringUtils.split(plainInput);
    }


    private Map<String, String> getParamMap(String input) {
        Map<String, String> map = new HashMap<>();
        String[] split = StringUtils.split(input);

        for (String s : split) {
            // 如果s是形如 k=v这种形式的，那么就将其放入map中
            if (StringUtils.contains(s, "=")) {
                String[] kv = StringUtils.split(s, "=");
                if (kv.length == 2) {
                    map.put(StringUtils.lowerCase(kv[0]), kv[1]);
                }
            }
        }
        return map;
    }

    public Optional<String> getOrDefaultAtIndex(String key, int defaultIndex) {
        Optional<String> opt = get(key);
        if (opt.isPresent()) {
            return opt;
        }
        if (paramSplit.length > defaultIndex) {
            return Optional.of(paramSplit[defaultIndex]);
        }
        return Optional.empty();
    }

    public Optional<String> getOrDefaultAtIndex(List<String> keys, int defaultIndex) {
        Optional<String> opt = get(keys);
        if (opt.isPresent()) {
            return opt;
        }
        if (paramSplit.length > defaultIndex) {
            return Optional.of(paramSplit[defaultIndex]);
        }
        return Optional.empty();
    }

    public Optional<String> getOrDefaultValue(String key, String defaultValue) {
        Optional<String> opt = get(key);
        if (opt.isPresent()) {
            return opt;
        }
        return Optional.of(defaultValue);
    }

    public Optional<String> getOrDefaultValue(List<String> keys, String defaultValue) {
        Optional<String> opt = get(keys);
        if (opt.isPresent()) {
            return opt;
        }
        return Optional.of(defaultValue);
    }

    public Optional<String> get(String key) {
        key = StringUtils.lowerCase(key);
        String val = paramMap.get(key);
        return Optional.ofNullable(val);
    }

    public Optional<String> get(List<String> keys) {
        for (String key : keys) {
            Optional<String> opt = get(key);
            if (opt.isPresent()) {
                return opt;
            }
        }
        return Optional.empty();
    }
}
