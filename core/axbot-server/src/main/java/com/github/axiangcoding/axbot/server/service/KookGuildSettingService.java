package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.repository.KookGuildSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KookGuildSettingService {
    @Resource
    private KookGuildSettingRepository kookGuildSettingRepository;

    public Optional<KookGuildSetting> findBytGuildId(String guildId) {
        return kookGuildSettingRepository.findByGuildId(guildId);
    }

    /**
     * 加入群组时操作
     *
     * @param guildId
     */
    public void updateWhenJoin(String guildId) {
        Optional<KookGuildSetting> opt = findBytGuildId(guildId);
        if (opt.isPresent()) {
            opt.get().setActive(true);
            kookGuildSettingRepository.save(opt.get());
            return;
        }
        KookGuildSetting entity = new KookGuildSetting();
        entity.setGuildId(guildId);
        entity.setBanned(false);
        entity.setActive(true);
        kookGuildSettingRepository.save(entity);
    }

    /**
     * 退出群组操作
     *
     * @param guildId
     */
    public void updateWhenExit(String guildId) {
        Optional<KookGuildSetting> opt = findBytGuildId(guildId);
        if (opt.isPresent()) {
            int updated = kookGuildSettingRepository.updateActiveByGuildId(false, guildId);
            System.out.println(updated);
        }
    }
}
