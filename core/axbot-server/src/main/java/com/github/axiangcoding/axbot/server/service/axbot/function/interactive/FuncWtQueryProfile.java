package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.data.entity.basic.BindProfile;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.WTGamerProfileService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FuncWtQueryProfile extends AbstractInteractiveFunction {
    @Resource
    WTGamerProfileService wtGamerProfileService;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Resource
    FuncWtUpdateProfile funcWtUpdateProfile;

    @Resource
    BotConfProps botConfProps;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String nickname = getUserNickname(input, SupportPlatform.KOOK);
        if (StringUtils.isEmpty(nickname)) {
            return input.response(kookInvalidNickname(botConfProps.getDefaultTriggerPrefix()));
        }
        Optional<WtGamerProfile> optGp = wtGamerProfileService.findByNickname(nickname);
        if (optGp.isEmpty()) {
            boolean canBeRefresh = wtGamerProfileService.canBeRefresh(nickname);
            if (!canBeRefresh) {
                return input.response(kookProfileNotFound(nickname, "未找到该玩家"));
            }
            return funcWtUpdateProfile.execute(input);
        } else {
            return input.response(funcWtUpdateProfile.kookProfileFound(nickname, optGp.get()));
        }
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        String nickname = getUserNickname(input, SupportPlatform.CQHTTP);
        if (StringUtils.isEmpty(nickname)) {
            return input.response(cqhttpInvalidNickname(botConfProps.getDefaultTriggerPrefix()));
        }
        Optional<WtGamerProfile> optGp = wtGamerProfileService.findByNickname(nickname);
        if (optGp.isEmpty()) {
            boolean canBeRefresh = wtGamerProfileService.canBeRefresh(nickname);
            if (!canBeRefresh) {
                return input.response(cqhttpProfileNotFound(nickname, "未找到该玩家"));
            }
            return funcWtUpdateProfile.execute(input);
        } else {
            return input.response(funcWtUpdateProfile.cqhttpProfileFound(nickname, optGp.get()));
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

    public static String kookProfileNotFound(String nickname, String moreMsg) {
        KookQuickCard quickCard = new KookQuickCard("战雷玩家 %s 的数据".formatted(nickname), "warning");
        quickCard.addModule(KookCardMessage.quickMdSection(moreMsg));
        return quickCard.displayWithFooter();
    }

    public static String cqhttpProfileNotFound(String nickname, String moreMsg) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("战雷玩家 %s 的数据".formatted(nickname));
        quickMsg.addLine(moreMsg);
        return quickMsg.displayWithFooter();
    }

    public static String kookInvalidNickname(String prefix) {
        KookQuickCard quickCard = new KookQuickCard("不正确的战雷玩家昵称", "warning");
        quickCard.addModuleMdSection("请检查你的昵称是否输入正确");
        quickCard.addModuleDivider();
        quickCard.addModuleMdSection("如果使用的是快捷查询，请先进行绑定哦");
        quickCard.addModuleMdSection("绑定命令为 %s".formatted(
                KookMDMessage.code(prefix + " 战雷 绑定 <昵称>")));
        return quickCard.displayWithFooter();
    }

    public static String cqhttpInvalidNickname(String prefix) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("不正确的战雷玩家昵称");
        quickMsg.addLine("请检查你的昵称是否输入正确");
        quickMsg.addLine("如果使用的是快捷查询，请先进行绑定哦");
        quickMsg.addLine("绑定命令为 `%s 战雷 绑定 <昵称>`".formatted(prefix));
        return quickMsg.displayWithFooter();
    }
}
