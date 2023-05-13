package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncUserBanned extends InteractiveFunction {
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
