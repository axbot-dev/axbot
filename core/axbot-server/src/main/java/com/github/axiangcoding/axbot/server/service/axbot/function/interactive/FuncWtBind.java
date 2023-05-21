package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class FuncWtBind extends InteractiveFunction {
    @Resource
    BotConfProps botConfProps;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String nickname = getUserNickname(input);
        if (StringUtils.isBlank(nickname)) {
            return input.response(kookBindFailed("请输入正确的昵称"));
        }
        kookUserSettingService.bindWtNickname(input.getUserId(), nickname);
        return input.response(kookBindSuccess());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        String nickname = getUserNickname(input);
        if (StringUtils.isBlank(nickname)) {
            return input.response(cqhttpBindFailed("请输入正确的昵称"));
        }
        qUserSettingService.bindWtNickname(input.getUserId(), nickname);
        return input.response(cqhttpBindSuccess());
    }

    private String getUserNickname(InteractiveInput input) {
        String[] paramList = input.getParamList();
        return StringUtils.join(paramList, " ");
    }

    private String kookBindSuccess() {
        KookQuickCard card = new KookQuickCard("快捷查询绑定成功", "success");
        card.addModuleMdSection("您已成功绑定快捷查询功能");
        card.addModuleMdSection("接下来如果你的查询不指定昵称，那么就将快捷查询绑定的战雷玩家哦");
        card.addModuleMdSection("请注意，这个绑定没有特殊意义，只是为了方便你使用快捷查询功能");
        card.addModuleMdSection("如果你想解除绑定，可以使用 %s 进行解绑".formatted(
                KookMDMessage.code(botConfProps.getDefaultTriggerPrefix() + " 战雷 解绑")
        ));
        return card.displayWithFooter();
    }

    private String cqhttpBindSuccess() {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("快捷查询绑定成功");
        msg.addLine("您已成功绑定快捷查询功能");
        msg.addLine("接下来如果你的查询不指定昵称，那么就将快捷查询绑定的战雷玩家哦");
        msg.addLine("请注意，这个绑定没有特殊意义，只是为了方便你使用快捷查询功能");
        msg.addLine("如果你想解除绑定，可以使用 `%s` 进行解绑".formatted(
                botConfProps.getDefaultTriggerPrefix() + " 战雷 解绑"
        ));
        return msg.displayWithFooter();
    }

    private String kookBindFailed(String info) {
        KookQuickCard card = new KookQuickCard("快捷查询绑定失败", "warning");
        card.addModuleMdSection(info);
        return card.displayWithFooter();
    }

    private String cqhttpBindFailed(String info) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("快捷查询绑定失败");
        msg.addLine(info);
        return msg.displayWithFooter();
    }
}
