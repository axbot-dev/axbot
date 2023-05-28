package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.WtGamerTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WtGamerTagRepository extends JpaRepository<WtGamerTag, Long> {
    List<WtGamerTag> findByNicknameAndReporterUserIdAndReporterPlatformAndTagAndCreateTimeAfter(
            String nickname,
            String reporterUserId,
            String reporterPlatform,
            String tag,
            LocalDateTime createTime);

    List<WtGamerTag> findByNicknameAndCreateTimeAfter(String nickname, LocalDateTime createTime);


}