package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.server.service.QGroupSettingService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FuncGuildStatus extends InteractiveFunction {
    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    QGroupSettingService qGroupSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        Optional<KookGuildSetting> opt = kookGuildSettingService.findBytGuildId(input.getGuildId());
        return opt.map(setting -> input.response(kookFound(setting))).orElse(null);
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        Optional<QGroupSetting> opt = qGroupSettingService.findByGroupId(input.getGroupId());
        return opt.map(qGroupSetting -> input.response(cqhttpFound(qGroupSetting))).orElse(null);
    }

    private String kookFound(KookGuildSetting setting) {
        KookQuickCard card = new KookQuickCard("当前服务器状态", "success");
        ArrayList<KookCardMessage> fields = new ArrayList<>();
        String template = "%s:\n%s";

        Map<String, Object> map = new LinkedHashMap<>();
        KookGuildSetting.FunctionSetting functionSetting = setting.getFunctionSetting();
        map.put("服务器ID", setting.getGuildId());
        map.put("服务器状态", setting.getBanned() ? "已拉黑" : "正常");
        map.put("战雷新闻播报状态", functionSetting.getEnableWtNewsReminder() ? "开启" : "关闭");
        map.put("战雷新闻播报频道", functionSetting.getWtNewsChannelId());
        map.put("B站直播通知状态", functionSetting.getEnableBiliLiveReminder() ? "开启" : "关闭");
        map.put("B站直播间", functionSetting.getBiliRoomId());
        map.put("B站直播通知频道", functionSetting.getBiliLiveChannelId());
        // map.put("禁用状态", setting.getBanned());
        map.forEach((k, v) -> {
            fields.add(KookCardMessage.newKMarkdown(template.formatted(KookMDMessage.bold(k), v)));
        });
        card.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(2, fields)));
        return card.displayWithFooter();
    }

    private String cqhttpFound(QGroupSetting qGroupSetting) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("当前服务器状态");
        msg.addLine("群号: %s".formatted(qGroupSetting.getGroupId()));
        msg.addLine("群状态: %s".formatted(qGroupSetting.getBanned() ? "已拉黑" : "正常"));
        return msg.displayWithFooter();
    }

}
