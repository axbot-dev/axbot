package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.RemoteClientService;
import com.github.axiangcoding.axbot.server.service.WTGamerProfileService;
import com.github.axiangcoding.axbot.server.service.WtGamerTagService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FuncWtReportGamer extends InteractiveFunction {
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

        Optional<WtGamerProfile> opt = wtGamerProfileService.findByNickname(nickname);
        if (opt.isEmpty()) {
            return input.response(kookUserNotFound(nickname));
        }
        if (!wtGamerTagService.canMarkTag(nickname, tag, input.getUserId(), SupportPlatform.KOOK)) {
            return input.response(kookReportTooFrequent(nickname));
        }
        wtGamerTagService.markTag(nickname, tag, input.getUserId(), SupportPlatform.KOOK);
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
        CqhttpQuickMsg msg = new CqhttpQuickMsg("暂不支持在Q上举办玩家");
        return input.response(msg.displayWithFooter());
    }

    private String getNickname(String[] paramList) {
        return StringUtils.join(Arrays.copyOfRange(paramList, 1, paramList.length), " ");
    }

    private String kookNotValidCommand() {
        KookQuickCard card = new KookQuickCard("无效的举办命令", "warning");
        String content = "请使用如下格式举报玩家：%s";
        String command = botConfProps.getDefaultTriggerPrefix() + " 战雷 举办 <类型> <玩家昵称>";
        card.addModuleMdSection(content.formatted(KookMDMessage.code(command)));
        List<String> supportTag = wtGamerTagService.getSupportTag();
        List<String> tagListCode = supportTag.stream().map(KookMDMessage::code).toList();
        card.addModuleMdSection("支持的类型有：" + String.join("、", tagListCode));
        card.addModuleDivider();
        card.addModuleMdSection("注意：请先确保玩家资料已在AXBot中，才能进行举办");
        return card.displayWithFooter();
    }

    private String kookUserNotFound(String nickname) {
        KookQuickCard card = new KookQuickCard("该玩家不存在", "warning");
        String command = botConfProps.getDefaultTriggerPrefix() + " 战雷 查询 " + nickname;
        card.addModuleMdSection("AXBot不接受对不存在在bot数据库中的玩家进行举办，尽管他可能真实存在");
        card.addModuleMdSection("请先使用如下命令查询玩家资料：%s".formatted(KookMDMessage.code(command)));
        return card.displayWithFooter();
    }

    private String kookReportTooFrequent(String nickname) {
        KookQuickCard card = new KookQuickCard("举办过于频繁", "warning");
        card.addModuleMdSection("你在最近7天内已经举办过该玩家这个行为了");
        card.addModuleMdSection("请不要频繁举办同一个玩家同一个行为");
        return card.displayWithFooter();
    }

    private String kookReportSuccess(String nickname, String tag) {
        KookQuickCard card = new KookQuickCard("举办成功", "success");
        card.addModule(KookCardMessage.quickContent("在AXBot中的任何举报仅为玩家对玩家的标识，不代表Gaijin官方意见，AXBot同时也不对举报的真实性负责"));
        card.addModuleMdSection("已成功举办玩家：%s, 标记为：%s".formatted(KookMDMessage.code(nickname), KookMDMessage.code(tag)));

        if (wtGamerTagService.isDangerTag(tag)) {
            String url = "https://warthunder.com/zh/tournament/replay/type/replays?Filter%5Bnick%5D=" + nickname;
            card.addModuleDivider();
            card.addModule(KookCardMessage.quickTextLinkSection("点击查看该玩家的近期服务器回放，在回放中举报才能引起官方重视哦", "服务器回放", "primary", url));
        }

        return card.displayWithFooter();
    }

    private String kookBeReport(String nickname) {
        KookQuickCard card = new KookQuickCard("AXBot温馨提示", "success");
        card.addModuleMdSection("AXBot非常遗憾地通知你，你绑定的战雷账号 %s 被其他用户举办了".formatted(
                KookMDMessage.code(nickname)
        ));
        card.addModuleMdSection("请注意，被其他用户举办，仅会在AXBot查询时显示标记，并不具备任何强制效力");
        card.addModuleMdSection("如果你认为这个标记会影响你的声誉，请加入AXBot服务器进行申诉消除");
        card.addModule(KookCardMessage.quickTextLinkSection("进入Kook服务器，到#问题反馈和意见建议频道进行反馈", "进入KOOK服务器", "primary", "https://kook.top/eUTZK7"));

        return card.displayWithFooter();
    }
}
