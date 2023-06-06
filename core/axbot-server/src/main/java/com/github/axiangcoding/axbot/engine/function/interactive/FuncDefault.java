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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@AxbotInteractiveFunc(command = InteractiveCommand.DEFAULT)
@Component
public class FuncDefault extends AbstractInteractiveFunction {
    @Resource
    BotConfProps botConfProps;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard quickCard = new KookQuickCard("你好，我是AXBot", "success");

        String l1 = "现在是北京时间: "
                + KookMDMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String prefix = botConfProps.getDefaultTriggerPrefix();
        String l2 = "需要我为你提供什么服务呢？如果你不知道怎么开始，聊天框输入 "
                + KookMDMessage.code("%s 帮助".formatted(prefix)) + " 开始探索";

        quickCard.addModuleMdSection(l1);
        String[] paramList = input.getParamList();
        if (paramList.length != 0) {
            quickCard.addModule(KookCardMessage.quickMdSection("你似乎输入了一个错误的命令：%s".formatted(
                    KookMDMessage.code(StringUtils.join(paramList, " "))
            )));
        }
        quickCard.addModuleMdSection(l2);
        quickCard.addModuleDivider();
        quickCard.addModuleInviteBot("邀请机器人加入你的服务器！");

        return input.response(quickCard.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("你好，我是AXBot");
        String prefix = botConfProps.getDefaultTriggerPrefix();
        quickMsg.addLine("现在是北京时间: %s".formatted(LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        String[] paramList = input.getParamList();
        if (paramList.length != 0) {
            quickMsg.addLine("你似乎输入了一个错误的命令：`%s`".formatted(StringUtils.join(paramList, " ")));
        }
        quickMsg.addLine("需要我为你提供什么服务呢？如果你不知道怎么开始，聊天框输入 `%s 帮助` 开始探索".formatted(prefix));

        return input.response(quickMsg.displayWithFooter());
    }
}
