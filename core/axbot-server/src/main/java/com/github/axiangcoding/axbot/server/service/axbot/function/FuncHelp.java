package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
public class FuncHelp extends InteractiveFunction {
    @Resource
    BotConfProps botConfProps;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard quickCard = new KookQuickCard("AXBot 帮助手册", "success");
        quickCard.addModule(KookCardMessage.newHeader("常用命令"));
        quickCard.addModule(KookCardMessage.newContext(List.of(KookCardMessage.newPlainText("以形如 “axbot <命令> [参数]”的形式调用"))));

        commandDescription(botConfProps.getTriggerMessagePrefix().get(0)).forEach((k, v) -> {
            String msg = KookMDMessage.code(k) + " - " + v;
            quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newKMarkdown(msg)));
        });

        quickCard.addModule(KookCardMessage.newDivider());
        quickCard.addModule(KookCardMessage.newHeader("完整命令"));
        quickCard.addModule(KookCardMessage.quickTextLinkSection("上面列出的只是常用命令的常用调用形式，更多调用方式请点击按钮查看文档",
                "跳转到文档", "info", "https://github.com/axiangcoding/AXBot/blob/master/docs/user_guide.md"));

        quickCard.addModule(KookCardMessage.newDivider());
        return input.response(quickCard.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("AXBot 帮助手册");
        quickMsg.addLine("常用命令");
        commandDescription(botConfProps.getTriggerMessagePrefix().get(0)).forEach((k, v) -> {
            String msg = "`%s` - %s".formatted(k, v);
            quickMsg.addLine(msg);
        });

        quickMsg.addLine("-----");
        quickMsg.addLine("完整命令");
        quickMsg.addLine("上面列出的只是常用命令的常用调用形式，更多调用方式请查看文档");
        quickMsg.addLine("https://github.com/axiangcoding/AXBot/blob/master/docs/user_guide.md");

        return input.response(quickMsg.displayWithFooter());
    }

    private static LinkedHashMap<String, String> commandDescription(String prefix) {
        LinkedHashMap<String, String> commandMap = new LinkedHashMap<>();
        commandMap.put("%s 气运".formatted(prefix), "获取今天的气运值");
        commandMap.put("%s 战雷 查询 <玩家昵称>".formatted(prefix), "查询战雷的玩家数据");
        commandMap.put("%s 战雷 刷新 <玩家昵称>".formatted(prefix), "刷新战雷的玩家数据");
        commandMap.put("%s 帮助".formatted(prefix), "获取帮助手册");
        commandMap.put("%s 状态".formatted(prefix), "查看个人的信息");
        commandMap.put("%s 服务器状态状态".formatted(prefix), "查看当前所处的Kook服务器的信息");
        commandMap.put("%s 管理 <管理命令>".formatted(prefix), "管理本Kook服务器的配置，服务器管理员角色可用");
        commandMap.put("%s 版本".formatted(prefix), "获取当前机器人的部署版本");
        commandMap.put("%s 赞助".formatted(prefix), "赞助机器人");

        return commandMap;
    }
}
