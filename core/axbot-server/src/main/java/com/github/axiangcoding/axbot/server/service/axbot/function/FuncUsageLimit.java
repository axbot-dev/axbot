package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FuncUsageLimit extends InteractiveFunction {
    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Resource
    FuncUserBanned funcUserBanned;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String userId = input.getUserId();
        Optional<KookUserSetting> opt = kookUserSettingService.findByUserId(userId);
        if (opt.isEmpty()) {
            return null;
        }
        int inputLimit = kookUserSettingService.getInputLimit(userId);
        int inputToday = opt.get().getUsage().getInputToday();

        if (inputToday > inputLimit + 5) {
            kookUserSettingService.blockUser(userId, "使用超出限制5次以上");
            return funcUserBanned.execute(input);
        }

        KookQuickCard card = new KookQuickCard("使用超限", "warning");
        card.addModule(KookCardMessage.quickMdSection("你在本日可使用次数已经超限，第二天重置"));
        card.addModule(KookCardMessage.quickMdSection("当日使用：%s / %s".formatted(
                KookMDMessage.code(String.valueOf(inputToday)), KookMDMessage.code(String.valueOf(inputLimit))
        )));
        return input.response(card.displayWithFooter());

    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        String userId = input.getUserId();
        Optional<QUserSetting> opt = qUserSettingService.findByUserId(userId);
        if (opt.isEmpty()) {
            return null;
        }
        int inputLimit = qUserSettingService.getInputLimit(userId);
        int inputToday = opt.get().getUsage().getInputToday();

        if (inputToday > inputLimit + 5) {
            qUserSettingService.blockUser(userId, "使用超出限制5次以上");
            return funcUserBanned.execute(input);
        }

        CqhttpQuickMsg msg = new CqhttpQuickMsg("使用超限");
        msg.addLine("你在本日可使用次数已经超限，第二天重置");
        msg.addLine("当日使用：%s / %s".formatted(inputToday, inputLimit));

        return input.response(msg.displayWithFooter());
    }
}
