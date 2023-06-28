package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUserInputRecord;
import com.github.axiangcoding.axbot.app.server.data.repo.EndUserInputRecordRepository;
import com.github.axiangcoding.axbot.app.server.data.repo.EndUserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class EndUserService {
    @Resource
    EndUserRepository repository;

    @Resource
    EndUserInputRecordRepository endUserInputRecordRepository;

    public EndUser getOrCreate(String authorId, BotPlatform platform) {
        String pn = platform.name();
        return repository.findByUserIdAndPlatform(authorId, pn).orElseGet(() -> {
            EndUser entity = EndUser.init(authorId, pn);
            return repository.save(entity);
        });
    }

    public long recordInput(BotPlatform platform, String guildId, String channelId,
                            String authorId, String input, UserCmd cmd) {
        EndUserInputRecord entity = new EndUserInputRecord()
                .setUserId(authorId)
                .setPlatform(platform.name())
                .setInput(input)
                .setCommand(cmd.name())
                .setGuildId(guildId)
                .setChannelId(channelId)
                .setIsSensitive(false);
        endUserInputRecordRepository.save(entity);
        return entity.getId();
    }

}
