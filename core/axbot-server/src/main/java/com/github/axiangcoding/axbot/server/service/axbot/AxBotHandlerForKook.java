package com.github.axiangcoding.axbot.server.service.axbot;

import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookPermission;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookRole;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookUser;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateMessageReq;
import com.github.axiangcoding.axbot.remote.kook.service.entity.resp.GuildRoleListData;
import com.github.axiangcoding.axbot.crawler.wt.entity.ParserResult;
import com.github.axiangcoding.axbot.engine.IAxBotHandlerForKook;
import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotUserOutputForKook;
import com.github.axiangcoding.axbot.server.configuration.props.KookConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.server.service.MissionService;
import com.github.axiangcoding.axbot.server.service.WTGameProfileService;
import com.github.axiangcoding.axbot.server.service.axbot.function.*;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class AxBotHandlerForKook implements IAxBotHandlerForKook {
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

    @Resource
    KookConfProps kookConfProps;

    @Override
    public String getDefault() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AXBot");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("现在是北京时间: "
                + KookMDMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))));

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("需要我为你提供什么服务呢？如果你不知道怎么开始，聊天框输入 "
                + KookMDMessage.code("axbot 帮助") + "开始探索")));

        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(
                KookCardMessage.newKMarkdown(
                        KookMDMessage.colorful("更多功能正在开发中！", "warning"))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("开发者B站主页，欢迎关注：" +
                KookMDMessage.link("https://space.bilibili.com/8696650")
        )));

        return JsonUtils.toJson(messages);
    }

    @Override
    public String getHelp() {
        return HelpFunction.helpCard(kookConfProps.getTriggerMessagePrefix().get(0));
    }

    @Override
    public String getVersion() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AxBot", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("当前版本为: "
                + KookMDMessage.code(System.getenv("APP_VERSION")))));

        return JsonUtils.toJson(messages);
    }

    @Override
    public String getTodayLucky(long seed) {
        return LuckyFunction.todayLucky(seed);
    }

    @Override
    public String commandNotFound(String unknownCmd) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AxBot", "warning");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("未识别的命令: "
                + KookMDMessage.code(unknownCmd))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果你不知道怎么开始，聊天框输入 "
                + KookMDMessage.code("axbot 帮助") + "开始探索")));

        return JsonUtils.toJson(messages);
    }

    @Override
    public String queryWTProfile(String nickname, AxBotUserOutput output) {
        Optional<WtGamerProfile> optGp = wtGameProfileService.findByNickname(nickname);
        if (optGp.isEmpty()) {
            return updateWTProfile(nickname, output);
        } else {
            return WTFunction.profileFound(nickname, optGp.get());
        }
    }

    @Override
    public String updateWTProfile(String nickname, AxBotUserOutput output) {
        if (!wtGameProfileService.canBeRefresh(nickname)) {
            return WTFunction.profileNotFoundMsg(nickname, "该玩家近期已更新过，更新间隔不能小于1天哦");
        } else {
            Mission oldM = wtGameProfileService.submitMissionToUpdate(nickname);
            UUID missionId = oldM.getMissionId();
            threadPoolTaskExecutor.execute(() -> {
                AxBotUserOutputForKook out = (AxBotUserOutputForKook) output;
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
                        ParserResult result = JsonUtils.fromJson(optM.get().getResult(), ParserResult.class);
                        if (!result.getFound()) {
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
                + KookMDMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("很高兴见到大家，当你看到这条信息时，我已经初始化完毕，即刻可以使用！")));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果你不知道怎么开始，聊天框输入 "
                + KookMDMessage.code("axbot 帮助") + "开始探索")));

        return JsonUtils.toJson(messages);
    }

    @Override
    public void exitGuild(String guildId) {
        kookGuildSettingService.updateWhenExit(guildId);
    }

    @Override
    public String biliLiveRemind(Long roomId, String title, String areaName, String description) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("AXBot的B站直播提醒", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newHeader("直播间: %s".formatted(title)));
        modules.add(KookCardMessage.newContext(List.of(KookCardMessage.newKMarkdown(description))));

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("直播间链接: %s".formatted(
                KookMDMessage.link("https://live.bilibili.com/%d".formatted(roomId))
        ))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("大家快来一起观看吧 " + KookMDMessage.mention("here"))));
        return JsonUtils.toJson(messages);
    }

    @Override
    public String manageGuild(String userId, String guildId, String channelId, String command) {
        KookResponse<KookUser> userView = kookClient.getUserView(userId, guildId);
        List<Long> userRoles = userView.getData().getRoles();

        KookResponse<GuildRoleListData> guildRoleList = kookClient.getGuildRoleList(guildId, null, null);
        List<KookRole> roles = guildRoleList.getData().getItems();

        boolean isAdmin = false;
        // 判断该用户的角色是否具备管理员权限
        for (KookRole role : roles) {
            if (userRoles.contains(role.getRoleId())) {
                if (KookPermission.hasPermission(role.getPermissions(), KookPermission.ADMIN)) {
                    isAdmin = true;
                    break;
                }
            }
        }
        String nickname = userView.getData().getNickname();

        if (!isAdmin) {
            log.info("user [{}] has no permission to manage guild [{}]", userId, guildId);
            return ManageFunction.noPermission(nickname);
        }

        String[] cmdList = StringUtils.split(command);
        String cmdPrefix = kookConfProps.getTriggerMessagePrefix().get(0);
        if (cmdList.length < 3) {
            return ManageFunction.getHelp(nickname, cmdPrefix);
        }

        // TODO 解析命令，管理社群配置
        String manageCmd = cmdList[1];
        switch (manageCmd) {
            case "帮助" -> {
                return ManageFunction.getHelp(nickname, cmdPrefix);
            }
            case "B站直播通知" -> {
                HashMap<String, Object> items = new HashMap<>();
                if ("关".equals(cmdList[2])) {
                    kookGuildSettingService.disableBiliRoomRemind(guildId);
                    items.put("测试", "test");
                    return ManageFunction.configSuccess(nickname, items);
                } else if ("开".equals(cmdList[2]) && cmdList.length == 4) {
                    kookGuildSettingService.enableBiliRoomRemind(guildId, channelId, cmdList[3]);
                    return ManageFunction.configSuccess(nickname, items);
                } else {
                    return ManageFunction.configError(nickname);
                }
            }
            case "战雷新闻播报" -> {

            }
            case "战雷战绩查询" -> {

            }
            default -> {
                return ManageFunction.configError(nickname);
            }
        }
        return ManageFunction.configError(nickname);
    }
}
