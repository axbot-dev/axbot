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
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.data.entity.basic.BindProfile;
import com.github.axiangcoding.axbot.server.service.*;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class FuncWtUpdateProfile extends InteractiveFunction {
    @Resource
    WTGameProfileService wtGameProfileService;
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

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String nickname = getUserNickname(input, SupportPlatform.KOOK);
        if (StringUtils.isEmpty(nickname)) {
            return input.response(FuncWtQueryProfile.kookInvalidNickname(botConfProps.getDefaultTriggerPrefix()));
        }

        if (!wtGameProfileService.canBeRefresh(nickname)) {
            return input.response(FuncWtQueryProfile.kookProfileNotFound(nickname, "该玩家近期已更新过，更新间隔不能小于1天哦"));
        } else {
            Mission oldM = wtGameProfileService.submitMissionToUpdate(nickname);
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
                            Optional<WtGamerProfile> opt = wtGameProfileService.findByNickname(nickname);
                            if (opt.isPresent()) {
                                content = FuncWtQueryProfile.kookProfileFound(nickname, opt.get());
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

        if (!wtGameProfileService.canBeRefresh(nickname)) {
            return input.response(FuncWtQueryProfile.cqhttpProfileNotFound(nickname, "该玩家近期已更新过，更新间隔不能小于1天哦"));
        } else {
            Mission oldM = wtGameProfileService.submitMissionToUpdate(nickname);
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
                            Optional<WtGamerProfile> opt = wtGameProfileService.findByNickname(nickname);
                            if (opt.isPresent()) {
                                content = FuncWtQueryProfile.cqhttpProfileFound(nickname, opt.get());
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
}
