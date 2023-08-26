package com.github.axiangcoding.axbot.app.bot.function.passive.game;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.function.ParamExtract;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.PubgPlayer;
import com.github.axiangcoding.axbot.app.server.data.entity.Task;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.pubg.PubgGameStats;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.pubg.PubgLifetimeStats;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.pubg.PubgPlayerInfo;
import com.github.axiangcoding.axbot.app.server.service.PubgPlayerService;
import com.github.axiangcoding.axbot.app.server.service.TaskService;
import com.github.axiangcoding.axbot.app.server.service.entity.SyncPlayerTaskResult;
import com.github.axiangcoding.axbot.app.third.pubg.PubgClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.event.ChannelMessageEvent;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.util.*;

@AxPassiveFunc(command = FunctionType.PUBG_QUERY_PLAYER)
@Slf4j
public class FuncPUBGQueryPlayer extends AbstractPassiveFunction {
    @Resource
    PubgPlayerService pubgPlayerService;

    @Resource
    TaskService taskService;


    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        ParamExtract paramExtract = getParamExtract(event);

        Optional<String> optPlayerName = paramExtract.getOrDefaultAtIndex(List.of("player", "id"), 2);
        Optional<String> optPlatform = paramExtract.getOrDefaultValue(List.of("platform", "平台"), PubgClient.PLATFORM.STEAM.getText());
        Optional<String> optMode = paramExtract.getOrDefaultValue(List.of("mode", "模式"), "solo");
        // 参数错误，直接返回错误信息
        if (optPlayerName.isEmpty() || optPlatform.isEmpty() || optMode.isEmpty()) {
            event.replyBlocking(toCardMessage(kookWrongInput().displayWithFooter()));
            return;
        }
        String playerName = optPlayerName.get();
        String platform = optPlatform.get();
        String gameMode = optMode.get();
        // 如果找到数据，则返回账号数据。同时判断是否可用自动同步玩家数据，可用即自动同步玩家数据
        Optional<PubgPlayer> opt = pubgPlayerService.findByPlayerName(playerName);
        if (opt.isPresent()) {
            PubgPlayer pubgPlayer = opt.get();
            String additionInfo = null;
            if (pubgPlayerService.allowSync(platform, playerName)) {
                String taskId = pubgPlayerService.startSyncTask(platform, null, playerName);
                pubgPlayerService.asyncSyncPlayer(taskId);
                additionInfo = "数据已过时，将自动和PUBG官方同步最新数据，同步进行中...";
            }
            event.replyBlocking(toCardMessage(kookQueryFound(pubgPlayer, gameMode, additionInfo).displayWithFooter()));
            return;
        }
        // 如果没找到数据，则发起一个异步的同步任务
        // 先判断是否允许同步玩家数据，不允许则直接返回错误信息
        boolean allowSync = pubgPlayerService.allowSync(platform, playerName);
        if (!allowSync) {
            event.replyBlocking(toCardMessage(kookQueryFailed("频繁查询").displayWithFooter()));
            return;
        }
        // 开始同步玩家数据流程
        event.replyBlocking(toCardMessage(kookQuerying().displayWithFooter()));
        String taskId = pubgPlayerService.startSyncTask(platform, null, playerName);
        pubgPlayerService.asyncSyncPlayer(taskId);
        int i = 0;
        int maxRetry = 10;
        int duration = 3000;
        for (; i < maxRetry; i++) {
            Optional<Task> optT = taskService.findByTaskId(taskId);
            if (optT.isPresent()) {
                Task task = optT.get();
                String status = task.getStatus();
                String result = task.getResult();
                if (Task.STATUS.FINISHED.name().equals(status)) {
                    SyncPlayerTaskResult res = JSONObject.parseObject(result, SyncPlayerTaskResult.class);
                    Optional<PubgPlayer> optPP = pubgPlayerService.findByPlayerId(res.getPlayerId());
                    if (optPP.isEmpty()) {
                        event.replyBlocking(toCardMessage(kookQueryFailed("数据丢失").displayWithFooter()));
                        break;
                    }
                    event.replyBlocking(toCardMessage(kookQueryFound(optPP.get(), gameMode).displayWithFooter()));
                    break;
                } else if (Task.STATUS.FAILED.name().equals(status)) {
                    event.replyBlocking(toCardMessage(kookQueryFailed(result).displayWithFooter()));
                    break;
                }
            }
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                log.warn("Polling hibernation is interrupted", e);
            }
        }

        if (i == maxRetry) {
            event.replyBlocking(toCardMessage(kookQueryFailed("轮询超时").displayWithFooter()));
        }
    }

    private KOOKCardTemplate kookWrongInput() {
        KOOKCardTemplate ct = new KOOKCardTemplate("参数错误", "danger");
        ct.addModuleMdSection("你输入了错误的查询参数，请参考使用文档");
        return ct;
    }

    private KOOKCardTemplate kookQuerying() {
        KOOKCardTemplate ct = new KOOKCardTemplate("正在查询中....", "success");
        ct.addModuleMdSection("正在查询中....");
        LocalDateTime now = LocalDateTime.now();
        ct.addCountDown("second", now, now.plusSeconds(10));
        return ct;
    }

    private KOOKCardTemplate kookQueryFound(PubgPlayer pubgPlayer, String gameMode) {
        return kookQueryFound(pubgPlayer, gameMode, null);
    }

    private KOOKCardTemplate kookQueryFound(PubgPlayer pubgPlayer, String gameMode, String additionInfo) {
        PubgPlayerInfo info = pubgPlayer.getInfo();
        PubgLifetimeStats lifetimeStats = pubgPlayer.getLifetimeStats();
        Map<String, Object> map = new LinkedHashMap<>();
        String playerName = pubgPlayer.getPlayerName();
        map.put("用户昵称", playerName);
        map.put("平台", info.getShardId());

        String banStr = switch (info.getBanType()) {
            case "Innocent" -> KOOKMDMessage.colorful("无封禁", "success");
            case "TemporaryBan" -> KOOKMDMessage.colorful("临时封禁", "warning");
            case "PermanentBan" -> KOOKMDMessage.colorful("永久封禁", "danger");
            default -> "未知";
        };

        map.put("封禁状态", banStr);
        map.put("和官网的同步时间", new PrettyTime(Locale.CHINA).format(pubgPlayer.getUpdateTime()));

        KOOKCardTemplate ct = new KOOKCardTemplate("绝地求生玩家 %s 的数据".formatted(playerName), "success");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ct.addKVLine(entry.getKey(), entry.getValue().toString());
        }

        if (List.of("duo", "双排").contains(gameMode)) {
            ct.addModuleDivider();
            ct.addModuleHeader("双排数据");
            generateStatsMap(lifetimeStats.getDuo()).forEach((k, v) -> ct.addKVLine(k, v.toString()));
        } else if (List.of("duo-fpp", "第一人称双排").contains(gameMode)) {
            ct.addModuleDivider();
            ct.addModuleHeader("双排（第一人称）数据");
            generateStatsMap(lifetimeStats.getDuoFpp()).forEach((k, v) -> ct.addKVLine(k, v.toString()));
        } else if (List.of("solo", "单排").contains(gameMode)) {
            ct.addModuleDivider();
            ct.addModuleHeader("单排数据");
            generateStatsMap(lifetimeStats.getSolo()).forEach((k, v) -> ct.addKVLine(k, v.toString()));
        } else if (List.of("solo-fpp", "第一人称单排").contains(gameMode)) {
            ct.addModuleDivider();
            ct.addModuleHeader("单排（第一人称）数据");
            generateStatsMap(lifetimeStats.getSoloFpp()).forEach((k, v) -> ct.addKVLine(k, v.toString()));
        } else if (List.of("squad", "四排").contains(gameMode)) {
            ct.addModuleDivider();
            ct.addModuleHeader("四排数据");
            generateStatsMap(lifetimeStats.getSquad()).forEach((k, v) -> ct.addKVLine(k, v.toString()));
        } else if (List.of("squad-fpp", "第一人称四排").contains(gameMode)) {
            ct.addModuleDivider();
            ct.addModuleHeader("四排（第一人称）数据");
            generateStatsMap(lifetimeStats.getSquad()).forEach((k, v) -> ct.addKVLine(k, v.toString()));
        } else {
            ct.addModuleDivider();
            ct.addModuleMdSection("模式 %s 无数据可显示".formatted(KOOKMDMessage.code(gameMode)));
        }

        ct.addModuleDivider();
        ct.addModuleMdSection("受限于文本长度，没法一次性展示全部数据");
        ct.addModuleMdSection("你可以在指令中使用参数来切换不同模式的数据");

        if (additionInfo != null) {
            ct.addModuleDivider();
            ct.addModuleMdSection(additionInfo);
        }
        return ct;
    }


    private KOOKCardTemplate kookQueryFailed(String reason) {
        KOOKCardTemplate ct = new KOOKCardTemplate("查询失败", "warning");
        ct.addModuleMdSection(reason);
        return ct;
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        // TODO 集成到qq频道上
    }

    private Map<String, Object> generateStatsMap(PubgGameStats gameStats) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("比赛场次", gameStats.getRoundsPlayed());
        map.put("前10场次", gameStats.getTop10s());
        map.put("获胜场次", gameStats.getWins());
        map.put("失败场次", gameStats.getLosses());

        map.put("击杀数", gameStats.getKills());
        map.put("击倒人数", gameStats.getDBNOs());
        map.put("协助击杀", gameStats.getAssists());
        map.put("爆头击杀数", gameStats.getHeadshotKills());
        map.put("击杀队友数", gameStats.getTeamKills());
        map.put("自尽次数", gameStats.getSuicides());
        map.put("造成的总伤害", gameStats.getDamageDealt());
        map.put("复活队友次数", gameStats.getRevives());

        map.put("最近一天的击杀数", gameStats.getDailyKills());
        map.put("最近一天比赛的胜场数", gameStats.getDailyWins());
        map.put("最近一周击杀数", gameStats.getWeeklyKills());
        map.put("最近一周比赛的胜场数", gameStats.getWeeklyWins());

        return map;
    }
}
