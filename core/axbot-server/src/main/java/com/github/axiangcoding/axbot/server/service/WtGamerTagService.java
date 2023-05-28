package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerTag;
import com.github.axiangcoding.axbot.server.data.repository.WtGamerTagRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class WtGamerTagService {
    @Resource
    WtGamerTagRepository wtGamerTagRepository;


    private static final List<String> DANGER_TAG = List.of("开挂", "脚本");
    private static final List<String> NORMAL_TAG = List.of("TK", "挂机", "辱骂");

    public boolean isValidTag(String tag) {
        List<String> allTag = Stream.of(DANGER_TAG, NORMAL_TAG).flatMap(List::stream).toList();
        return allTag.contains(tag);
    }

    public List<String> getSupportTag() {
        return Stream.of(DANGER_TAG, NORMAL_TAG).flatMap(List::stream).toList();
    }

    public boolean isDangerTag(String tag) {
        return DANGER_TAG.contains(tag);
    }

    public void markTag(String nickname, String tag, String userId, SupportPlatform platform) {
        WtGamerTag entity = new WtGamerTag();
        entity.setNickname(nickname);
        entity.setTag(tag);
        entity.setReporterUserId(userId);
        entity.setReporterPlatform(platform.getLabel());
        wtGamerTagRepository.save(entity);
    }

    public List<WtGamerTag> findLatestTag(String nickname) {
        return wtGamerTagRepository.findByNicknameAndCreateTimeAfter(nickname, LocalDateTime.now().minusDays(30));
    }

    public boolean canMarkTag(String nickname, String tag, String userId, SupportPlatform platform) {
        List<WtGamerTag> list = wtGamerTagRepository.findByNicknameAndReporterUserIdAndReporterPlatformAndTagAndCreateTimeAfter(
                nickname,
                userId,
                platform.getLabel(),
                tag,
                LocalDateTime.now().minusDays(7)
        );
        return list.isEmpty();
    }
}
