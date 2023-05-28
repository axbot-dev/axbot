package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.crawler.wt.entity.ProfileParseResult;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.*;
import com.github.axiangcoding.axbot.server.data.entity.basic.BindProfile;
import com.github.axiangcoding.axbot.server.service.*;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FuncWtUpdateProfile extends InteractiveFunction {
    @Resource
    WTGamerProfileService wtGamerProfileService;
    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    MissionService missionService;

    @Resource
    RemoteClientService remoteClientService;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Resource
    BotConfProps botConfProps;

    @Resource
    WtGamerTagService wtGamerTagService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String nickname = getUserNickname(input, SupportPlatform.KOOK);
        if (StringUtils.isEmpty(nickname)) {
            return input.response(FuncWtQueryProfile.kookInvalidNickname(botConfProps.getDefaultTriggerPrefix()));
        }

        if (!wtGamerProfileService.canBeRefresh(nickname)) {
            return input.response(FuncWtQueryProfile.kookProfileNotFound(nickname, "该玩家近期已更新过，更新间隔不能小于1天哦"));
        } else {
            Mission oldM = wtGamerProfileService.submitMissionToUpdate(nickname);
            UUID missionId = oldM.getMissionId();
            threadPoolTaskExecutor.execute(() -> {
                try {
                    int i = 0;
                    int maxTimes = 12;
                    String content = null;
                    while (i < maxTimes) {
                        Optional<Mission> optM = missionService.findByMissionId(missionId.toString());
                        if (optM.isEmpty()) {
                            continue;
                        }
                        String status = optM.get().getStatus();
                        if (Mission.STATUS_FAILED.equals(status)) {
                            log.warn("queue at {} times, mission failed", i);
                            content = kookProfileQueryFailed(nickname);

                            break;
                        }
                        if (Mission.STATUS_SUCCESS.equals(status)) {
                            ProfileParseResult result = JsonUtils.fromJson(optM.get().getResult(), ProfileParseResult.class);
                            if (!result.getFound()) {
                                log.warn("queue at {} times, mission success, but not found", i);
                                content = FuncWtQueryProfile.kookProfileNotFound(nickname, "找不到该用户的数据");
                                break;
                            }
                            Optional<WtGamerProfile> opt = wtGamerProfileService.findByNickname(nickname);
                            if (opt.isPresent()) {
                                content = kookProfileFound(nickname, opt.get());
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
                        content = FuncWtQueryProfile.kookProfileNotFound(nickname, "查询轮询超时，请稍后重试");

                    }
                    remoteClientService.sendKookCardMsg(input.response(content));
                } catch (Exception e) {
                    log.error("async update wt profile error", e);
                }
            });
            return input.response(kookProfileInQuery(nickname));
        }


    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        String nickname = getUserNickname(input, SupportPlatform.CQHTTP);
        if (StringUtils.isEmpty(nickname)) {
            return input.response(FuncWtQueryProfile.kookInvalidNickname(botConfProps.getDefaultTriggerPrefix()));
        }

        if (!wtGamerProfileService.canBeRefresh(nickname)) {
            return input.response(FuncWtQueryProfile.cqhttpProfileNotFound(nickname, "该玩家近期已更新过，更新间隔不能小于1天哦"));
        } else {
            Mission oldM = wtGamerProfileService.submitMissionToUpdate(nickname);
            UUID missionId = oldM.getMissionId();
            threadPoolTaskExecutor.execute(() -> {
                try {
                    int i = 0;
                    int maxTimes = 12;
                    String content = null;
                    while (i < maxTimes) {
                        Optional<Mission> optM = missionService.findByMissionId(missionId.toString());
                        if (optM.isEmpty()) {
                            continue;
                        }
                        String status = optM.get().getStatus();
                        if (Mission.STATUS_FAILED.equals(status)) {
                            log.warn("queue at {} times, mission failed", i);
                            content = cqhttpProfileQueryFailed(nickname);

                            break;
                        }
                        if (Mission.STATUS_SUCCESS.equals(status)) {
                            ProfileParseResult result = JsonUtils.fromJson(optM.get().getResult(), ProfileParseResult.class);
                            if (!result.getFound()) {
                                log.warn("queue at {} times, mission success, but not found", i);
                                content = FuncWtQueryProfile.cqhttpProfileNotFound(nickname, "找不到该用户的数据");
                                break;
                            }
                            Optional<WtGamerProfile> opt = wtGamerProfileService.findByNickname(nickname);
                            if (opt.isPresent()) {
                                content = cqhttpProfileFound(nickname, opt.get());
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
                        content = FuncWtQueryProfile.cqhttpProfileNotFound(nickname, "查询轮询超时，请稍后重试");

                    }
                    remoteClientService.sendCqhttpMsg(input.response(content));
                } catch (Exception e) {
                    log.error("async update wt profile error", e);
                }
            });
            return input.response(cqhttpProfileInQuery(nickname));
        }
    }

    private String getUserNickname(InteractiveInput input, SupportPlatform platform) {
        String[] paramList = input.getParamList();
        String nickname = StringUtils.join(paramList, " ");
        if (StringUtils.isBlank(nickname)) {
            if (platform == SupportPlatform.KOOK) {
                Optional<KookUserSetting> opt = kookUserSettingService.findByUserId(input.getUserId());
                if (opt.isPresent()) {
                    BindProfile bindProfile = opt.get().getBindProfile();
                    if (bindProfile != null) {
                        nickname = bindProfile.getWtNickname();
                    }
                }
            } else if (platform == SupportPlatform.CQHTTP) {
                Optional<QUserSetting> opt = qUserSettingService.findByUserId(input.getUserId());
                if (opt.isPresent()) {
                    BindProfile bindProfile = opt.get().getBindProfile();
                    if (bindProfile != null) {
                        nickname = bindProfile.getWtNickname();
                    }
                }
            }
        }
        return nickname;
    }

    private String kookProfileInQuery(String nickname) {
        KookQuickCard card = new KookQuickCard("战雷玩家 %s 的数据".formatted(nickname), "success");
        card.addModule(KookCardMessage.quickMdSection("正在发起查询...请耐心等待"));
        LocalDateTime now = LocalDateTime.now();
        card.addModule(KookCardMessage.newCountDown("second", now, now.plusSeconds(60)));
        return card.displayWithFooter();
    }

    private String cqhttpProfileInQuery(String nickname) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("战雷玩家 %s 的数据".formatted(nickname));
        msg.addLine("正在发起查询...请耐心等待");
        return msg.displayWithFooter();
    }

    private String kookProfileQueryFailed(String nickname) {
        KookQuickCard card = new KookQuickCard("战雷玩家 %s 的数据".formatted(nickname), "danger");
        card.addModule(KookCardMessage.quickMdSection("查询失败！请稍后重试"));
        return card.displayWithFooter();
    }

    private String cqhttpProfileQueryFailed(String nickname) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("战雷玩家 %s 的数据".formatted(nickname));
        msg.addLine("查询失败！请稍后重试");
        return msg.displayWithFooter();
    }

    public String kookProfileFound(String nickname, WtGamerProfile profile) {
        KookQuickCard quickCard = new KookQuickCard("战雷玩家 %s 的数据".formatted(nickname), "success");
        quickCard.addModule(KookCardMessage.newContext(
                List.of(KookCardMessage.newKMarkdown("一切数据均来自战雷官网，AXBot只做计算不做修改"))));
        quickCard.addModule(KookCardMessage.newContext(
                List.of(KookCardMessage.newKMarkdown("数据最后更新于 %s".formatted(
                        new PrettyTime(Locale.CHINA).format(profile.getUpdateTime()))))));

        ArrayList<KookCardMessage> f1 = new ArrayList<>();

        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("联队") + "\n" + profile.getClan()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("昵称") + "\n" + profile.getNickname()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("称号") + "\n" + profile.getTitle()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("等级") + "\n" + profile.getLevel()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("注册时间") + "\n" + profile.getRegisterDate()));
        if (profile.getBanned()) {
            f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("当前状态") + "\n" + KookMDMessage.colorful("已封禁", "danger")));
        }


        WtGamerProfile.UserStat ab = profile.getStatAb();
        WtGamerProfile.UserStat sb = profile.getStatSb();
        WtGamerProfile.UserStat rb = profile.getStatRb();

        DecimalFormat df = new DecimalFormat("0.00");
        double abKd = 1.0 * (ab.getGroundDestroyCount() + ab.getFleetDestroyCount() + ab.getAviationDestroyCount()) / ab.getDeadCount();
        double sbKd = 1.0 * (sb.getGroundDestroyCount() + sb.getFleetDestroyCount() + sb.getAviationDestroyCount()) / sb.getDeadCount();
        double rbKd = 1.0 * (rb.getGroundDestroyCount() + rb.getFleetDestroyCount() + rb.getAviationDestroyCount()) / rb.getDeadCount();

        ArrayList<KookCardMessage> f2 = new ArrayList<>();
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("街机任务数") + "\n" + ab.getTotalMission()));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("街机胜率") + "\n" + NumberFormat.getPercentInstance().format(ab.getWinRate())));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("街机KD") + "\n" + df.format(abKd)));

        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("史实任务数") + "\n" + rb.getTotalMission()));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("史实胜率") + "\n" + NumberFormat.getPercentInstance().format(rb.getWinRate())));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("史实KD") + "\n" + df.format(rbKd)));

        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("全真任务数") + "\n" + sb.getTotalMission()));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("全真胜率") + "\n" + NumberFormat.getPercentInstance().format(sb.getWinRate())));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("全真KD") + "\n" + df.format(sbKd)));

        ArrayList<KookCardMessage> f3 = new ArrayList<>();
        WtGamerProfile.AviationRate aab = profile.getAviationRateAb();
        WtGamerProfile.AviationRate arb = profile.getAviationRateRb();
        WtGamerProfile.AviationRate asb = profile.getAviationRateSb();
        double aabKa = 1.0 * aab.getTotalDestroyCount() / aab.getGameCount();
        double arbKa = 1.0 * arb.getTotalDestroyCount() / arb.getGameCount();
        double asbKa = 1.0 * asb.getTotalDestroyCount() / asb.getGameCount();


        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("空战街机KA") + "\n" + df.format(aabKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("空战史实KA") + "\n" + df.format(arbKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("空战全真KA") + "\n" + df.format(asbKa)));

        WtGamerProfile.GroundRate gab = profile.getGroundRateAb();
        WtGamerProfile.GroundRate grb = profile.getGroundRateRb();
        WtGamerProfile.GroundRate gsb = profile.getGroundRateSb();
        double gabKa = 1.0 * gab.getTotalDestroyCount() / gab.getGameCount();
        double grbKa = 1.0 * grb.getTotalDestroyCount() / grb.getGameCount();
        double gsbKa = 1.0 * gsb.getTotalDestroyCount() / gsb.getGameCount();

        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("陆战街机KA") + "\n" + df.format(gabKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("陆战史实KA") + "\n" + df.format(grbKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("陆战全真KA") + "\n" + df.format(gsbKa)));


        WtGamerProfile.FleetRate fab = profile.getFleetRateAb();
        WtGamerProfile.FleetRate frb = profile.getFleetRateRb();
        WtGamerProfile.FleetRate fsb = profile.getFleetRateSb();
        double fabKa = 1.0 * fab.getTotalDestroyCount() / fab.getGameCount();
        double frbKa = 1.0 * frb.getTotalDestroyCount() / frb.getGameCount();
        double fsbKa = 1.0 * fsb.getTotalDestroyCount() / fsb.getGameCount();
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("海战街机KA") + "\n" + df.format(fabKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("海战史实KA") + "\n" + df.format(frbKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("海战全真KA") + "\n" + df.format(fsbKa)));


        quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f1)));
        quickCard.addModule(KookCardMessage.newDivider());
        quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f2)));
        quickCard.addModule(KookCardMessage.newDivider());
        quickCard.addModule(KookCardMessage.newContext(List.of(KookCardMessage.newKMarkdown("击杀数/出击数简称为'KA'"))));
        quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f3)));


        List<WtGamerTag> latestTag = wtGamerTagService.findLatestTag(nickname);
        if (latestTag.size() > 0) {
            quickCard.addModuleDivider();
            quickCard.addModuleMdSection("该玩家在最近30天的标记");
            quickCard.addModuleContentSection("以下标记均为用户标记，不代表Gaijin官方意见，同时AXBot不对真实性负责");

            // 将latestTag中的属性tag一样的合并为一项，并附加X1,x2类似的标记
            StringBuilder content = new StringBuilder();
            Map<String, List<WtGamerTag>> tagMap = latestTag.stream().collect(Collectors.groupingBy(WtGamerTag::getTag));
            tagMap.forEach((tag, tags) -> {
                if (wtGamerTagService.isDangerTag(tag)) {
                    tag = "疑似" + tag;
                }
                String tagName = "%s x %d".formatted(tag, tags.size());
                content.append(KookMDMessage.code(tagName)).append(" ");

            });
            quickCard.addModuleMdSection(content.toString());
        }


        return quickCard.displayWithFooter();
    }

    public String cqhttpProfileFound(String nickname, WtGamerProfile profile) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("战雷玩家 %s 的数据".formatted(nickname));
        quickMsg.addLine("一切数据均来自战雷官网，AXBot只做计算不做修改");
        quickMsg.addLine("数据最后更新于 %s".formatted(
                new PrettyTime(Locale.CHINA).format(profile.getUpdateTime())));
        quickMsg.addLine("联队: %s".formatted(profile.getClan()));
        quickMsg.addLine("昵称: %s".formatted(profile.getNickname()));
        quickMsg.addLine("等级: %s".formatted(profile.getLevel()));
        quickMsg.addLine("称号: %s".formatted(profile.getTitle()));
        quickMsg.addLine("注册时间: %s".formatted(profile.getRegisterDate()));
        if (profile.getBanned()) {
            quickMsg.addLine("当前状态: %s".formatted("封禁中"));
        }
        quickMsg.addDivider();


        WtGamerProfile.UserStat ab = profile.getStatAb();
        WtGamerProfile.UserStat sb = profile.getStatSb();
        WtGamerProfile.UserStat rb = profile.getStatRb();

        DecimalFormat df = new DecimalFormat("0.00");
        double abKd = 1.0 * (ab.getGroundDestroyCount() + ab.getFleetDestroyCount() + ab.getAviationDestroyCount()) / ab.getDeadCount();
        double sbKd = 1.0 * (sb.getGroundDestroyCount() + sb.getFleetDestroyCount() + sb.getAviationDestroyCount()) / sb.getDeadCount();
        double rbKd = 1.0 * (rb.getGroundDestroyCount() + rb.getFleetDestroyCount() + rb.getAviationDestroyCount()) / rb.getDeadCount();
        quickMsg.addLine("街机任务数: %s".formatted(ab.getTotalMission()));
        quickMsg.addLine("街机胜率: %s".formatted(NumberFormat.getPercentInstance().format(ab.getWinRate())));
        quickMsg.addLine("街机KD: %s".formatted(df.format(abKd)));
        quickMsg.addLine("史实任务数: %s".formatted(rb.getTotalMission()));
        quickMsg.addLine("史实胜率: %s".formatted(NumberFormat.getPercentInstance().format(rb.getWinRate())));
        quickMsg.addLine("史实KD: %s".formatted(df.format(rbKd)));
        quickMsg.addLine("全真任务数: %s".formatted(sb.getTotalMission()));
        quickMsg.addLine("全真胜率: %s".formatted(NumberFormat.getPercentInstance().format(sb.getWinRate())));
        quickMsg.addLine("全真KD: %s".formatted(df.format(sbKd)));
        quickMsg.addDivider();
        ArrayList<KookCardMessage> f3 = new ArrayList<>();
        WtGamerProfile.AviationRate aab = profile.getAviationRateAb();
        WtGamerProfile.AviationRate arb = profile.getAviationRateRb();
        WtGamerProfile.AviationRate asb = profile.getAviationRateSb();
        double aabKa = 1.0 * aab.getTotalDestroyCount() / aab.getGameCount();
        double arbKa = 1.0 * arb.getTotalDestroyCount() / arb.getGameCount();
        double asbKa = 1.0 * asb.getTotalDestroyCount() / asb.getGameCount();

        quickMsg.addLine("空战街机KA: %s".formatted(df.format(aabKa)));
        quickMsg.addLine("空战史实KA: %s".formatted(df.format(arbKa)));
        quickMsg.addLine("空战全真KA: %s".formatted(df.format(asbKa)));
        quickMsg.addDivider();

        WtGamerProfile.GroundRate gab = profile.getGroundRateAb();
        WtGamerProfile.GroundRate grb = profile.getGroundRateRb();
        WtGamerProfile.GroundRate gsb = profile.getGroundRateSb();
        double gabKa = 1.0 * gab.getTotalDestroyCount() / gab.getGameCount();
        double grbKa = 1.0 * grb.getTotalDestroyCount() / grb.getGameCount();
        double gsbKa = 1.0 * gsb.getTotalDestroyCount() / gsb.getGameCount();

        quickMsg.addLine("陆战街机KA: %s".formatted(df.format(gabKa)));
        quickMsg.addLine("陆战史实KA: %s".formatted(df.format(grbKa)));
        quickMsg.addLine("陆战全真KA: %s".formatted(df.format(gsbKa)));
        quickMsg.addDivider();

        WtGamerProfile.FleetRate fab = profile.getFleetRateAb();
        WtGamerProfile.FleetRate frb = profile.getFleetRateRb();
        WtGamerProfile.FleetRate fsb = profile.getFleetRateSb();
        double fabKa = 1.0 * fab.getTotalDestroyCount() / fab.getGameCount();
        double frbKa = 1.0 * frb.getTotalDestroyCount() / frb.getGameCount();
        double fsbKa = 1.0 * fsb.getTotalDestroyCount() / fsb.getGameCount();

        quickMsg.addLine("海战街机KA: %s".formatted(df.format(fabKa)));
        quickMsg.addLine("海战史实KA: %s".formatted(df.format(frbKa)));
        quickMsg.addLine("海战全真KA: %s".formatted(df.format(fsbKa)));
        return quickMsg.displayWithFooter();
    }
}
