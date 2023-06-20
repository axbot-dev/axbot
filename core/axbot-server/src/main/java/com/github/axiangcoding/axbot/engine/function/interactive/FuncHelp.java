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
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@AxbotInteractiveFunc(command = InteractiveCommand.HELP)
@Component
public class FuncHelp extends AbstractInteractiveFunction {
    @Resource
    BotConfProps botConfProps;

    private static final String DOC_URL = "https://www.yuque.com/axiangcoding/ei27mo/omy4cgwvsikrwue1";

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard quickCard = new KookQuickCard("命令列表", "success");
        quickCard.addModule(KookCardMessage.newHeader("常用命令"));
        quickCard.addModuleContentSection("以形如 “axbot <命令> [参数]”的形式调用");

        commandDescription(botConfProps.getTriggerMessagePrefix().get(0)).forEach((k, v) -> {
            String msg = KookMDMessage.code(k) + " - " + v;
            quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newKMarkdown(msg)));
        });
        quickCard.addModuleDivider();
        quickCard.addModule(KookCardMessage.newHeader("完整命令"));
        quickCard.addModule(KookCardMessage.quickTextLinkSection("AXBot支持多级命令调用，上面列出的只是常用命令，完整内容和限制见使用手册",
                "使用手册", "primary", DOC_URL));
        quickCard.addModuleDivider();
        quickCard.addModuleInviteBot("邀请机器人加入你的服务器！");

        return input.response(quickCard.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("命令列表");
        quickMsg.addLine("常用命令");
        commandDescription(botConfProps.getTriggerMessagePrefix().get(0)).forEach((k, v) -> {
            String msg = "\"%s\" - %s".formatted(k, v);
            quickMsg.addLine(msg);
        });

        quickMsg.addLine("-----");
        quickMsg.addLine("完整命令");
        quickMsg.addLine("上面列出的只是常用命令，完整内容和限制见使用手册");
        quickMsg.addLine(DOC_URL);

        return input.response(quickMsg.display());
    }

    private static LinkedHashMap<String, String> commandDescription(String prefix) {
        LinkedHashMap<String, String> cmdMap = new LinkedHashMap<>();
        cmdMap.put("气运", "获取今天的气运值");
        cmdMap.put("抽卡", "抽一张塔罗牌");
        cmdMap.put("战雷 查询 <玩家昵称>", "查询战雷的玩家数据");
        cmdMap.put("战雷 刷新 <玩家昵称>", "刷新战雷的玩家数据");
        cmdMap.put("战雷 绑定 <玩家昵称>", "绑定战雷玩家到你的当前账号上");
        cmdMap.put("战雷 解绑", "解绑当前账号的战雷玩家");
        cmdMap.put("战雷 举报 <举报类型> <玩家昵称>", "举报（非官方）某个战雷玩家");
        cmdMap.put("帮助", "获取帮助手册");
        cmdMap.put("状态", "查看个人的信息");
        cmdMap.put("群状态", "查看当前所处的Kook服务器的信息");
        cmdMap.put("管理 <管理命令>", "管理本Kook服务器的配置，服务器管理员角色可用");
        cmdMap.put("版本", "获取当前机器人的部署版本");
        cmdMap.put("赞助", "赞助机器人");

        LinkedHashMap<String, String> commandMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : cmdMap.entrySet()) {
            String key = prefix + " " + entry.getKey();
            commandMap.put(key, entry.getValue());
        }
        return commandMap;
    }
}
