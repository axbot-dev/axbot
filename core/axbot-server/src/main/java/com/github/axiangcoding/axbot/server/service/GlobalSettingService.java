package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.GlobalSetting;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.repository.GlobalSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GlobalSettingService {
    @Resource
    GlobalSettingRepository globalSettingRepository;

    @Resource
    KookUserSettingService kookUserSettingService;

    public List<GlobalSetting> findAll() {
        return globalSettingRepository.findAll();
    }

    public Map<String, String> findAllAsMap() {
        Map<String, String> map = new HashMap<>();
        List<GlobalSetting> all = findAll();

        for (GlobalSetting globalSetting : all) {
            map.put(globalSetting.getKey(), globalSetting.getValue());
        }
        return map;
    }

    public Optional<GlobalSetting> findByKey(String key) {
        return globalSettingRepository.findByKey(key);
    }

    public void save(GlobalSetting gs) {
        globalSettingRepository.save(gs);
    }


}
