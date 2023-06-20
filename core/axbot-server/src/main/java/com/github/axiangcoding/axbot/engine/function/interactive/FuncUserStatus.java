package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserPermit;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserUsage;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@AxbotInteractiveFunc(command = InteractiveCommand.USER_STATUS)
@Component
public class FuncUserStatus extends AbstractInteractiveFunction {
    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String userId = input.getUserId();
        Optional<KookUserSetting> opt = kookUserSettingService.findByUserId(userId);
        return opt.map(kookUserSetting -> input.response(kookFound(kookUserSetting))).orElse(null);
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        String userId = input.getUserId();
        Optional<QUserSetting> opt = qUserSettingService.findByUserId(userId);
        return opt.map(kookUserSetting -> input.response(cqhttpFound(kookUserSetting))).orElse(null);
    }


    private String kookFound(KookUserSetting setting) {
        KookQuickCard card = new KookQuickCard("当前用户状态", "success");

        ArrayList<KookCardMessage> fields = new ArrayList<>();
        String template = "%s:\n%s";

        Map<String, Object> map = new LinkedHashMap<>();
        UserPermit permit = setting.getPermit();
        UserUsage usage = setting.getUsage();
        map.put("用户ID", setting.getUserId());
        map.put("用户状态", setting.getBanned() ? "已拉黑" : "正常");
        map.put("授权状态 - AI聊天功能", permit.getCanUseAIChat() ? "开启" : "关闭");
        map.put("使用情况 - 当日命令请求", "%d/%d".formatted(usage.getInputToday(), kookUserSettingService.getInputLimit(setting.getUserId())));
        map.put("使用情况 - 总共命令请求", usage.getInputTotal());

        map.forEach((k, v) -> {
            fields.add(KookCardMessage.newKMarkdown(template.formatted(KookMDMessage.bold(k), v)));
        });

        card.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(2, fields)));
        return card.displayWithFooter();
    }

    private String cqhttpFound(QUserSetting setting) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("当前用户状态");

        msg.addLine("用户ID: %s".formatted(setting.getUserId()));
        msg.addLine("用户状态: %s".formatted(setting.getBanned() ? "已拉黑" : "正常"));
        msg.addLine("授权状态 - AI聊天功能: %s".formatted(setting.getPermit().getCanUseAIChat() ? "开启" : "关闭"));
        msg.addLine("使用情况 - 当日命令请求: %d/%d".formatted(setting.getUsage().getInputToday(), qUserSettingService.getInputLimit(setting.getUserId())));
        msg.addLine("使用情况 - 总共命令请求: %d".formatted(setting.getUsage().getInputTotal()));

        return msg.display();
    }
}
