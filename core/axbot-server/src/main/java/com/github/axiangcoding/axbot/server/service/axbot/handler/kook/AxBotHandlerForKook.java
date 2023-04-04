package com.github.axiangcoding.axbot.server.service.axbot.handler.kook;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.bot.kook.KookClient;
import com.github.axiangcoding.axbot.bot.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.bot.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.bot.kook.entity.KookKMarkdownMessage;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CreateMessageReq;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.server.service.MissionService;
import com.github.axiangcoding.axbot.server.service.WTGameProfileService;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotOutput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotOutputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.handler.AxBotHandler;
import com.github.axiangcoding.axbot.server.service.axbot.handler.kook.function.HelpFunction;
import com.github.axiangcoding.axbot.server.service.axbot.handler.kook.function.LuckyFunction;
import com.github.axiangcoding.axbot.server.service.axbot.handler.kook.function.StatusFunction;
import com.github.axiangcoding.axbot.server.service.axbot.handler.kook.function.WTFunction;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class AxBotHandlerForKook implements AxBotHandler {
    @Resource
    WTGameProfileService wtGameProfileService;

    @Resource
    MissionService missionService;

    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    KookClient kookClient;

    @Override
    public String getDefault() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AXBot");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("现在是北京时间: "
                + KookKMarkdownMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))));

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("我可以怎么帮助你呢？如果你不知道怎么开始，聊天框输入 "
                + KookKMarkdownMessage.code("axbot 帮助") + "开始探索")));

        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(
                KookCardMessage.newKMarkdown("(font)更多功能正在开发中！敬请期待(font)[warning]")));

        return JSONObject.toJSONString(messages);
    }

    @Override
    public String getHelp() {
        return HelpFunction.helpCard();
    }

    @Override
    public String getVersion() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AxBot", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("当前版本为: "
                + KookKMarkdownMessage.code(System.getenv("APP_VERSION")))));

        return JSONObject.toJSONString(messages);
    }

    @Override
    public String getTodayLucky(long seed) {
        return LuckyFunction.todayLucky(seed);
    }

    @Override
    public String notMatch(String unknownCommand) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AxBot", "warning");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("未识别的命令: "
                + KookKMarkdownMessage.code(unknownCommand))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果你不知道怎么开始，聊天框输入 "
                + KookKMarkdownMessage.code("axbot 帮助") + "开始探索")));

        return JSONObject.toJSONString(messages);
    }

    @Override
    public String queryWTProfile(String nickname, AxBotOutput output) {
        Optional<WtGamerProfile> optGp = wtGameProfileService.findByNickname(nickname);
        if (optGp.isEmpty()) {
            return updateWTProfile(nickname, output);
        } else {
            return WTFunction.profileFound(nickname, optGp.get());
        }
    }

    @Override
    public String updateWTProfile(String nickname, AxBotOutput output) {
        if (!wtGameProfileService.canBeRefresh(nickname)) {
            return WTFunction.profileNotFoundMsg(nickname, "该玩家近期已更新过，更新间隔不能小于1天哦");
        } else {
            Mission oldM = wtGameProfileService.submitMissionToUpdate(nickname);
            UUID missionId = oldM.getMissionId();
            threadPoolTaskExecutor.execute(() -> {
                AxBotOutputForKook out = (AxBotOutputForKook) output;
                CreateMessageReq req = new CreateMessageReq();
                req.setType(KookEvent.TYPE_CARD);
                req.setQuote(out.getReplayToMsg());
                req.setTargetId(out.getToChannel());
                int i = 0;
                int maxTimes = 12;
                while (i < maxTimes) {
                    Optional<Mission> optM = missionService.findByMissionId(missionId.toString());
                    if (optM.isEmpty()) {
                        continue;
                    }
                    String status = optM.get().getStatus();
                    if (Mission.STATUS_FAILED.equals(status)) {
                        log.warn("queue at {} times, mission failed", i);
                        req.setContent(WTFunction.profileQueryFailed(nickname));
                        break;
                    }
                    if (Mission.STATUS_SUCCESS.equals(status)) {
                        if (!JSONObject.parseObject(optM.get().getResult()).getBooleanValue("found")) {
                            log.warn("queue at {} times, mission success, but not found", i);
                            req.setContent(WTFunction.profileNotFoundMsg(nickname));
                            break;
                        }
                        Optional<WtGamerProfile> opt = wtGameProfileService.findByNickname(nickname);
                        if (opt.isPresent()) {
                            req.setContent(WTFunction.profileFound(nickname, opt.get()));
                            break;
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    i++;
                }
                if (i == maxTimes) {
                    req.setContent(WTFunction.profileNotFoundMsg(nickname, "查询轮询超时，请稍后重试"));
                }
                kookClient.createMessage(req);
            });
            return WTFunction.profileInQuery(nickname);
        }
    }

    @Override
    public String getGuildStatus(String guildId) {
        Optional<KookGuildSetting> optKgs = kookGuildSettingService.findBytGuildId(guildId);
        if (optKgs.isEmpty()) {
            return StatusFunction.settingNotFound();
        } else {
            return StatusFunction.settingFound(optKgs.get());
        }
    }

    @Override
    public String joinGuild(String guildId) {

        kookGuildSettingService.updateWhenJoin(guildId);

        List<KookCardMessage> messages = KookCardMessage.defaultMsg("大家好，我是AxBot", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("现在是北京时间: "
                + KookKMarkdownMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("很高兴见到大家，当你看到这条信息时，我已经初始化完毕，即刻可以使用！")));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果你不知道怎么开始，聊天框输入 "
                + KookKMarkdownMessage.code("axbot 帮助") + "开始探索")));

        return JSONObject.toJSONString(messages);
    }

    @Override
    public void exitGuild(String guildId) {
        kookGuildSettingService.updateWhenExit(guildId);
    }
}
