package com.github.axiangcoding.axbot.app.bot.function.passive.game;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.PubgPlayer;
import com.github.axiangcoding.axbot.app.server.data.entity.Task;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.PubgPlayerInfo;
import com.github.axiangcoding.axbot.app.server.service.PubgPlayerService;
import com.github.axiangcoding.axbot.app.server.service.TaskService;
import com.github.axiangcoding.axbot.app.server.service.entity.SyncPlayerTaskResult;
import com.github.axiangcoding.axbot.app.third.pubg.PubgClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.event.ChannelMessageEvent;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@AxPassiveFunc(command = FunctionType.PUBG_QUERY_PLAYER)
@Slf4j
public class FuncPUBGQueryPlayer extends AbstractPassiveFunction {
    @Resource
    PubgPlayerService pubgPlayerService;

    @Resource
    TaskService taskService;

    private String getPlatform(String[] params) {
        if (params.length == 3) {
            return PubgClient.PLATFORM.STEAM.getText();
        } else if (params.length == 4) {
            return params[2];
        } else {
            return null;
        }
    }

    private String getPlayerName(String[] params) {
        if (params.length == 3) {
            return params[2];
        } else if (params.length == 4) {
            return params[3];
        } else {
            return null;
        }
    }

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String[] split = StringUtils.split(getInput(event));
        String playerName = getPlayerName(split);
        String platform = getPlatform(split);
        // 参数错误，直接返回错误信息
        if (StringUtils.isBlank(playerName) || StringUtils.isBlank(platform)) {
            event.replyBlocking(toCardMessage(kookWrongInput().displayWithFooter()));
            return;
        }
        // 如果找到数据，则返回账号数据。同时判断是否可用自动同步玩家数据，可用即自动同步玩家数据
        Optional<PubgPlayer> opt = pubgPlayerService.findByPlayerName(playerName);
        if (opt.isPresent()) {
            PubgPlayer pubgPlayer = opt.get();
            String additionInfo = null;
            if (pubgPlayerService.allowSync(platform, playerName)) {
                pubgPlayerService.startSyncTask(platform, null, playerName);
                additionInfo = "数据已过时，将自动和PUBG官方同步，稍后请重新查询最新数据";
            }
            event.replyBlocking(toCardMessage(kookQueryFound(pubgPlayer, additionInfo).displayWithFooter()));
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
                    event.replyBlocking(toCardMessage(kookQueryFound(optPP.get()).displayWithFooter()));
                    break;
                } else if (Task.STATUS.FAILED.name().equals(status)) {
                    event.replyBlocking(toCardMessage(kookQueryFailed(result).displayWithFooter()));
                    break;
                } else {
                    continue;
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

    private KOOKCardTemplate kookQueryFound(PubgPlayer pubgPlayer) {
        return kookQueryFound(pubgPlayer, null);
    }

    private KOOKCardTemplate kookQueryFound(PubgPlayer pubgPlayer, String additionInfo) {
        PubgPlayerInfo info = pubgPlayer.getInfo();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("用户ID", pubgPlayer.getPlayerId());
        map.put("用户昵称", pubgPlayer.getPlayerName());
        map.put("平台", info.getShardId());

        String banStr = switch (info.getBanType()) {
            case "Innocent" -> "无封禁";
            case "TemporaryBan" -> "临时封禁";
            case "PermanentBan" -> "永久封禁";
            default -> "未知";
        };

        map.put("封禁状态", banStr);
        map.put("联队ID", info.getClanId());
        map.put("工作室和游戏", info.getTitleId());
        map.put("游戏版本", info.getPatchVersion());


        KOOKCardTemplate ct = new KOOKCardTemplate("查询完成", "success");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ct.addKVLine(entry.getKey(), entry.getValue().toString());
        }
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
}
