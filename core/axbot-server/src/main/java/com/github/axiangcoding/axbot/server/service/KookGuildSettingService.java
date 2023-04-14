package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.repository.KookGuildSettingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KookGuildSettingService {

    @Resource
    KookGuildSettingRepository kookGuildSettingRepository;

    public Optional<KookGuildSetting> findBytGuildId(String guildId) {
        return kookGuildSettingRepository.findByGuildId(guildId);
    }

    public List<KookGuildSetting> findByEnabledBiliLiveReminder() {
        return kookGuildSettingRepository.findByFunctionSettingEnableBiliLiveReminder(true);
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
            kookGuildSettingRepository.updateActiveByGuildId(false, guildId);
        }
    }

    public void disableBiliRoomRemind(String guildId) {
        Optional<KookGuildSetting> opt = findBytGuildId(guildId);
        if (opt.isPresent()) {
            KookGuildSetting entity = opt.get();
            KookGuildSetting.FunctionSetting functionSetting = entity.getFunctionSetting();
            functionSetting.setBiliLiveChannelId(null);
            functionSetting.setBiliRoomId(null);
            functionSetting.setEnableBiliLiveReminder(false);
            kookGuildSettingRepository.save(entity);
        }
    }


    public void enableBiliRoomRemind(String guildId, String channelId, String roomId) {
        Optional<KookGuildSetting> opt = findBytGuildId(guildId);
        if (opt.isPresent()) {
            KookGuildSetting entity = opt.get();
            KookGuildSetting.FunctionSetting functionSetting = entity.getFunctionSetting();
            functionSetting.setEnableBiliLiveReminder(true);
            functionSetting.setBiliLiveChannelId(channelId);
            functionSetting.setBiliRoomId(roomId);
            kookGuildSettingRepository.save(entity);
        }
    }
}
