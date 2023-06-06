package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AxbotInteractiveFunc(command = InteractiveCommand.USER_BANNED)
@Component
public class FuncUserBanned extends AbstractInteractiveFunction {
    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard card = new KookQuickCard("你已被拉黑！", "danger");
        Optional<KookUserSetting> opt = kookUserSettingService.findByUserId(input.getUserId());
        String reason = "";
        if (opt.isPresent()) {
            reason = opt.get().getBannedReason();
        }
        card.addModule(KookCardMessage.quickMdSection("你已被拉黑！原因：%s".formatted(reason)));
        card.addModule(KookCardMessage.quickTextLinkSection("如果你对本封禁有任何异议，请申诉", "进入KOOK服务器申诉", "primary", "https://kook.top/eUTZK7"));
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("你已被拉黑！");
        Optional<QUserSetting> opt = qUserSettingService.findByUserId(input.getUserId());
        String reason = "";
        if (opt.isPresent()) {
            reason = opt.get().getBannedReason();
        }
        msg.addLine("你已被拉黑！原因：%s".formatted(reason));
        msg.addLine("如果你对本封禁有任何异议，请申诉");
        return input.response(msg.displayWithFooter());
    }
}
