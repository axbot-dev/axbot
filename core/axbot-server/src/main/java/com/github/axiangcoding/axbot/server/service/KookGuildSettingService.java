package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.repository.KookGuildSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KookGuildSettingService {
    @Resource
    KookGuildSettingRepository kookGuildSettingRepository;


    public Optional<KookGuildSetting> findBytGuildId(String guildId) {
        return kookGuildSettingRepository.findByGuildId(guildId);
    }
}
