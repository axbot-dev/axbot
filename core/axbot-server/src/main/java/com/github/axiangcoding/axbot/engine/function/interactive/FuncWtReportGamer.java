package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.SupportPlatform;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.RemoteClientService;
import com.github.axiangcoding.axbot.server.service.WTGamerProfileService;
import com.github.axiangcoding.axbot.server.service.WtGamerTagService;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AxbotInteractiveFunc(command = InteractiveCommand.WT_REPORT_GAMER)
@Component
@Slf4j
public class FuncWtReportGamer extends AbstractInteractiveFunction {
    @Resource
    WtGamerTagService wtGamerTagService;

    @Resource
    WTGamerProfileService wtGamerProfileService;

    @Resource
    BotConfProps botConfProps;

    @Resource
    RemoteClientService remoteClientService;

    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String[] paramList = input.getParamList();
        if (paramList.length < 2 || !wtGamerTagService.isValidTag(paramList[0])) {
            return input.response(kookNotValidCommand());
        }
        String tag = paramList[0];
        String nickname = getNickname(paramList);

        if (!wtGamerTagService.canMarkTag(nickname, tag, input.getUserId(), SupportPlatform.KOOK)) {
            return input.response(kookReportTooFrequent(nickname));
        }
        wtGamerTagService.markTag(nickname, tag, input.getUserId(), SupportPlatform.KOOK);

        Optional<WtGamerProfile> opt = wtGamerProfileService.findByNickname(nickname);
        if (opt.isEmpty()) {
            return input.response(kookUserNotFound(nickname));
        }

        threadPoolTaskExecutor.execute(() -> {
            List<KookUserSetting> kookUsers = kookUserSettingService.findByBindWtNickname(nickname);
            kookUsers.forEach(item -> {
                remoteClientService.sendKookPrivateCardMsg(item.getUserId(), kookBeReport(nickname));
            });

        });
        return input.response(kookReportSuccess(nickname, tag));
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("暂不支持在Q上举报玩家");
        return input.response(msg.displayWithFooter());
    }

    private String getNickname(String[] paramList) {
        return StringUtils.join(Arrays.copyOfRange(paramList, 1, paramList.length), " ");
    }

    private String kookNotValidCommand() {
        KookQuickCard card = new KookQuickCard("无效的举报命令", "warning");
        String content = "请使用如下格式举报玩家：%s";
        String command = botConfProps.getDefaultTriggerPrefix() + " 战雷 举报 <类型> <玩家昵称>";
        card.addModuleMdSection(content.formatted(KookMDMessage.code(command)));
        List<String> supportTag = wtGamerTagService.getSupportTag();
        List<String> tagListCode = supportTag.stream().map(KookMDMessage::code).toList();
        card.addModuleMdSection("支持的类型有：" + String.join("、", tagListCode));
        card.addModuleDivider();
        card.addModuleMdSection("注意：请先确保玩家资料已在AXBot中，才能进行举报");
        return card.displayWithFooter();
    }

    private String kookUserNotFound(String nickname) {
        KookQuickCard card = new KookQuickCard("举报成功，但该玩家数据不存在于AXBot数据库中", "success");
        String command = botConfProps.getDefaultTriggerPrefix() + " 战雷 查询 " + nickname;
        card.addModuleMdSection("AXBot已经接受了你的举报信息，但是该玩家并不存在于AXBot的数据库中");
        card.addModuleMdSection("为了确保举报到了正确的玩家，请使用如下命令查询玩家资料：%s".formatted(KookMDMessage.code(command)));
        return card.displayWithFooter();
    }

    private String kookReportTooFrequent(String nickname) {
        KookQuickCard card = new KookQuickCard("举报过于频繁", "warning");
        card.addModuleMdSection("你在最近7天内已经举报过该玩家这个行为了");
        card.addModuleMdSection("请不要频繁举报同一个玩家同一个行为");
        return card.displayWithFooter();
    }

    private String kookReportSuccess(String nickname, String tag) {
        KookQuickCard card = new KookQuickCard("举报成功", "success");
        card.addModule(KookCardMessage.quickContent("在AXBot中的任何举报仅为玩家对玩家的标识，不代表Gaijin官方意见，AXBot同时也不对举报的真实性负责"));
        card.addModuleMdSection("已成功举报玩家：%s, 标记为：%s".formatted(KookMDMessage.code(nickname), KookMDMessage.code(tag)));

        if (wtGamerTagService.isDangerTag(tag)) {
            String url = "https://warthunder.com/en/tournament/replay/type/replays?Filter%5Bnick%5D=" + nickname;
            card.addModuleDivider();
            card.addModule(KookCardMessage.quickTextLinkSection("点击查看该玩家的近期服务器录像，在回放中举报才能引起官方重视", "服务器回放", "primary", url));
        }

        return card.displayWithFooter();
    }

    private String kookBeReport(String nickname) {
        KookQuickCard card = new KookQuickCard("AXBot温馨提示", "success");
        card.addModuleMdSection("AXBot非常遗憾地通知你，你绑定的战雷账号 %s 被其他用户举报了".formatted(
                KookMDMessage.code(nickname)
        ));
        card.addModuleMdSection("请注意，被其他用户举报，仅会在AXBot查询时显示标记，并不具备任何强制效力");
        card.addModuleMdSection("如果你认为这个标记会影响你的声誉，请加入AXBot服务器进行申诉消除");
        card.addModuleGetHelp("可以到“#问题反馈和意见建议”频道进行申诉消除标记");
        return card.displayWithFooter();
    }
}
