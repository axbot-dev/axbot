package com.github.axiangcoding.axbot.server.service.axbot.handler;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.bot.kook.KookClient;
import com.github.axiangcoding.axbot.bot.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.bot.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.bot.kook.entity.KookKMarkdownMessage;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CreateMessageReq;
import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.service.MissionService;
import com.github.axiangcoding.axbot.server.service.WTGameProfileService;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotOutput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotOutputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.handler.function.WTFunction;
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
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    KookClient kookClient;


    @Override
    public String getDefault() {
        List<KookCardMessage> messages = new ArrayList<>();
        KookCardMessage card = KookCardMessage.newCard("info", "lg");
        ArrayList<KookCardMessage> modules = new ArrayList<>();

        modules.add(KookCardMessage.newHeader("你好，我是AxBot"));
        modules.add(KookCardMessage.newDivider());

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("现在是北京时间: "
                + KookKMarkdownMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))));

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("我可以怎么帮助你呢？如果你不知道怎么开始，聊天框输入 "
                + KookKMarkdownMessage.code("axbot 帮助") + "开始探索")));

        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(
                KookCardMessage.newKMarkdown("(font)更多功能正在开发中！敬请期待(font)[warning]")));

        card.setModules(modules);
        messages.add(card);
        return JSONObject.toJSONString(messages);
    }

    @Override
    public String getHelp() {
        List<KookCardMessage> messages = new ArrayList<>();
        KookCardMessage card = KookCardMessage.newCard("success", "lg");
        ArrayList<KookCardMessage> modules = new ArrayList<>();
        modules.add(KookCardMessage.newHeader("AXBot 帮助手册"));
        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newHeader("常用命令"));
        modules.add(KookCardMessage.newContext(List.of(KookCardMessage.newPlainText("以形如 “axbot [命令] [参数]”的格式调用"))));

        Map<String, String> commandMap = new HashMap<>();
        commandMap.put("axbot 气运", "获取今天的气运值");
        commandMap.put("axbot 帮助", "获取帮助手册");

        commandMap.forEach((k, v) -> {
            String msg = KookKMarkdownMessage.code(k) + " - " + v;
            modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(msg)));
        });

        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newHeader("完整命令"));
        modules.add(KookCardMessage.newSectionWithLink(
                KookCardMessage.newKMarkdown("请到 [TBD] 查看完整命令列表"),
                KookCardMessage.newButton("info", "跳转外部链接")));

        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(
                KookCardMessage.newKMarkdown("(font)更多功能正在开发中！敬请期待(font)[warning]")));

        card.setModules(modules);
        messages.add(card);
        return JSONObject.toJSONString(messages);
    }

    @Override
    public String getVersion() {
        List<KookCardMessage> messages = new ArrayList<>();
        KookCardMessage card = KookCardMessage.newCard("success", "lg");
        ArrayList<KookCardMessage> modules = new ArrayList<>();

        modules.add(KookCardMessage.newHeader("你好，我是AxBot"));
        modules.add(KookCardMessage.newDivider());

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("当前版本为: "
                + KookKMarkdownMessage.code(System.getenv("APP_VERSION")))));

        card.setModules(modules);
        messages.add(card);
        return JSONObject.toJSONString(messages);
    }

    @Override
    public String getTodayLucky(long seed) {
        List<KookCardMessage> messages = new ArrayList<>();
        KookCardMessage card = KookCardMessage.newCard("success", "lg");
        ArrayList<KookCardMessage> modules = new ArrayList<>();

        modules.add(KookCardMessage.newHeader("您的今日气运为：" + new Random(seed).nextInt(100)));
        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("再接再厉哦")));

        card.setModules(modules);
        messages.add(card);
        return JSONObject.toJSONString(messages);
    }

    @Override
    public String notMatch(String unknownCommand) {
        List<KookCardMessage> messages = new ArrayList<>();
        KookCardMessage card = KookCardMessage.newCard("warning", "lg");
        ArrayList<KookCardMessage> modules = new ArrayList<>();

        modules.add(KookCardMessage.newHeader("你好，我是AxBot"));
        modules.add(KookCardMessage.newDivider());

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("未识别的命令: "
                + KookKMarkdownMessage.code(unknownCommand))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果你不知道怎么开始，聊天框输入 "
                + KookKMarkdownMessage.code("axbot 帮助") + "开始探索")));


        card.setModules(modules);
        messages.add(card);
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
            return WTFunction.profileNotFoundMsg(nickname, "该玩家未找到，且近期已经查询过");
        } else {
            Mission oldM = wtGameProfileService.submitMissionToUpdate(nickname);
            UUID missionId = oldM.getMissionId();
            threadPoolTaskExecutor.execute(() -> {
                AxBotOutputForKook out = (AxBotOutputForKook) output;
                CreateMessageReq req = new CreateMessageReq();
                req.setType(KookEvent.TYPE_CARD);
                req.setQuote(out.getReplayToMsg());
                req.setTargetId(out.getReplayToChannel());
                int i = 0;
                while (i < 10) {
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
                if (i == 10) {
                    req.setContent(WTFunction.profileNotFoundMsg(nickname, "查询轮询超时，请稍后重试"));
                }
                kookClient.createMessage(req);
            });
            return WTFunction.profileInQuery(nickname);
        }
    }
}
