package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import love.forte.simbot.event.ChannelMessageEvent;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@AxPassiveFunc(command = UserCmd.DEFAULT)
public class FuncDefault extends AbstractPassiveFunction {

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        KOOKCardTemplate mt = new KOOKCardTemplate("你好，我是AXBot V2", "success");

        String l1 = "现在是北京时间: "
                + KOOKMDMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String l2 = "需要我做些什么呢？如果不知道怎么开始，可以@我，并输入 "
                + KOOKMDMessage.code("帮助") + " 开始探索";
        mt.addModuleMdSection(l1);
        String input = getInput(event);
        if (StringUtils.isNotBlank(input)) {
            mt.addModuleMdSection("你似乎输入了一个错误的指令：%s".formatted(
                    KOOKMDMessage.code(input)));
        }
        mt.addModuleMdSection(l2);
        mt.addModuleDivider();
        mt.addInviteBot("邀请机器人加入你的服务器！");
        event.replyAsync(toCardMessage(mt.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        QGContentTemplate ct = new QGContentTemplate("你好，我是AXBot");
        String l1 = "现在是北京时间: "
                + LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String l2 = "需要我做些什么呢？如果不知道怎么开始，可以@我，并输入 \"帮助\" 开始探索";
        ct.addLine(l1);
        String input = getInput(event);
        if (StringUtils.isNotBlank(input)) {
            ct.addLine("你似乎输入了一个错误的指令：\"%s\"".formatted(input));
        }
        ct.addLine(l2);
        event.replyAsync(toTextMessage(ct.displayWithFooter()));
    }
}
