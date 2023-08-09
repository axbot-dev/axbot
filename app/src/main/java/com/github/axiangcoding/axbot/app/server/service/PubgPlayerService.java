package com.github.axiangcoding.axbot.app.server.service;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.server.data.cache.CacheKeyGenerator;
import com.github.axiangcoding.axbot.app.server.data.entity.PubgPlayer;
import com.github.axiangcoding.axbot.app.server.data.entity.PubgPlayerSnapshot;
import com.github.axiangcoding.axbot.app.server.data.entity.Task;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.PubgPlayerInfo;
import com.github.axiangcoding.axbot.app.server.data.repo.PubgPlayerRepository;
import com.github.axiangcoding.axbot.app.server.data.repo.PubgPlayerSnapshotRepository;
import com.github.axiangcoding.axbot.app.server.service.entity.SyncPlayerTaskConfig;
import com.github.axiangcoding.axbot.app.server.service.entity.SyncPlayerTaskResult;
import com.github.axiangcoding.axbot.app.third.pubg.PubgClient;
import com.github.axiangcoding.axbot.app.third.pubg.service.entity.PubgResponse;
import com.github.axiangcoding.axbot.app.third.pubg.service.entity.resp.Player;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class PubgPlayerService {
    @Resource
    PubgPlayerRepository repository;

    @Resource
    PubgPlayerSnapshotRepository snapshotRepository;

    @Resource
    TaskService taskService;

    @Resource
    PubgClient pubgClient;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public Optional<PubgPlayer> findByPlayerId(String playerId) {
        return repository.findByPlayerId(playerId);
    }

    public Optional<PubgPlayer> findByPlayerName(String playerName) {
        return repository.findByPlayerName(playerName);
    }

    /**
     * 是否允许执行同步用户数据的请求
     *
     * @param playerName
     * @return
     */
    public boolean allowSync(String platform, String playerName) {
        String key = CacheKeyGenerator.getPubgPlayerSyncFrozenKey(platform, playerName);
        Boolean b = stringRedisTemplate.hasKey(key);
        return b == null || !b;
    }

    public void setFrozen(String platform, String playerName) {
        String key = CacheKeyGenerator.getPubgPlayerSyncFrozenKey(platform, playerName);
        stringRedisTemplate.opsForValue().set(key, "", Duration.ofDays(1));
    }

    public String startSyncTask(String platform, String playerId, String playerName) {
        String taskId = taskService.startNewTask(new SyncPlayerTaskConfig()
                .setPlatform(platform)
                .setPlayerId(playerId)
                .setPlayerName(playerName));
        asyncSyncPlayer(taskId);
        return taskId;
    }

    public void upsertInfo(String playerId, PubgPlayerInfo player) {
        Optional<PubgPlayer> opt = repository.findByPlayerId(playerId);
        if (opt.isEmpty()) {
            repository.save(new PubgPlayer()
                    .setPlayerId(playerId)
                    .setPlayerName(player.getName())
                    .setInfo(player));
        } else {
            PubgPlayer pubgPlayer = opt.get();
            repository.save(pubgPlayer
                    .setPlayerName(player.getName())
                    .setInfo(player));
        }
    }

    @Async
    public void asyncSyncPlayer(String taskId) {
        Optional<Task> opt = taskService.findByTaskId(taskId);
        if (opt.isEmpty()) {
            log.warn("task not found, taskId: {}", taskId);
            return;
        }
        Task task = opt.get();
        String config = task.getConfig();
        SyncPlayerTaskConfig conf = JSONObject.parseObject(config, SyncPlayerTaskConfig.class);
        String platform = conf.getPlatform();
        String playerId = conf.getPlayerId();
        String playerName = conf.getPlayerName();
        if (StringUtils.isNoneBlank(playerId) && StringUtils.isNoneBlank(playerName)) {
            taskService.failedWithResult(taskId, "playerId and playerName cannot be both set");
            return;
        }
        setFrozen(platform, playerName);
        try {
            PubgResponse<Player> players = pubgClient.getPlayers(platform, playerName, playerId);
            // TODO 集成玩家的生涯数据
            //  https://documentation.pubg.com/en/lifetime-stats.html#lifetime-stats
            Player player = players.getData().get(0);
            String id = player.getId();
            String name = player.getAttributes().getName();
            PubgPlayerInfo playerInfo = new PubgPlayerInfo()
                    .setName(name)
                    .setClanId(player.getAttributes().getClanId())
                    .setShardId(player.getAttributes().getShardId())
                    .setPatchVersion(player.getAttributes().getPatchVersion())
                    .setTitleId(player.getAttributes().getTitleId())
                    .setBanType(player.getAttributes().getBanType());
            PubgPlayerSnapshot snapshot = snapshotRepository.save(new PubgPlayerSnapshot()
                    .setPlayerId(id)
                    .setInfo(playerInfo));
            upsertInfo(id, playerInfo);

            taskService.finishedWithResult(taskId, new SyncPlayerTaskResult()
                    .setPlayerId(id)
                    .setSnapshotId(snapshot.getId()));
        } catch (HttpException e) {
            if (e.response().code() == 404) {
                taskService.failedWithResult(taskId, "玩家未找到");
            } else {
                log.warn("sync player failed, taskId: {}", taskId, e);
                taskService.failedWithResult(taskId, e.getMessage());
            }
        }

    }
}