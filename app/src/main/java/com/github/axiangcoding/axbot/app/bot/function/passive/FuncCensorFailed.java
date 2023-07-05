package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = UserCmd.CENSOR_FAILED)
public class FuncCensorFailed extends AbstractPassiveFunction {
    @Resource
    EndUserService endUserService;

    @Resource
    FuncUserBanned funcUserBanned;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String userId = event.getAuthor().getId().toString();
        long counted = endUserService.countLatestSensitiveInput(userId, BotPlatform.KOOK);
        long limit = 5;
        if (counted > limit) {
            endUserService.blockUser(userId, BotPlatform.KOOK, "输入5次以上违规的命令");
            funcUserBanned.processForKOOK(event);
            return;
        }
        KOOKCardTemplate ct = new KOOKCardTemplate("AXBot不会响应这条消息", "danger");
        ct.addModuleMdSection("本条消息的输入被AI判定为违规，因此机器人不会做出任何正常回复");
        ct.addModuleMdSection("最近一个月内如果累计5条消息被判定为违规，你将会被AXBot拉黑7天");
        ct.addModuleMdSection("当前你已达到 %s / %s".formatted(
                KOOKMDMessage.code(String.valueOf(counted)), KOOKMDMessage.code(String.valueOf(limit))));
        ct.addModuleMdSection("请注意：AXBot不对AI审核的结果负责");
        ct.addModuleDivider();
        ct.addGetHelp("如果你被误判拉黑，可以到服务器中的“#拉黑申述”频道申请解封");

        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        String userId = event.getAuthor().getId().toString();

        long counted = endUserService.countLatestSensitiveInput(userId, BotPlatform.QQ_GUILD);
        long limit = 5;
        if (counted > limit) {
            endUserService.blockUser(userId, BotPlatform.QQ_GUILD, "输入5次以上违规的命令");
            funcUserBanned.processForQG(event);
            return;
        }
        QGContentTemplate ct = new QGContentTemplate("AXBot不会响应这条消息");
        ct.addLine("本条消息的输入被AI判定为违规，因此机器人不会做出任何正常回复");
        ct.addLine("最近一个月内如果累计5条消息被判定为违规，你将会被AXBot拉黑");
        ct.addLine("当前你已达到 %d / %d".formatted(counted, limit));
        ct.addLine("请注意：AXBot不对AI审核的结果负责");
        ct.addDivider();
        ct.addLine("如果你被误判拉黑，请到KOOK频道申请解封");

        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }
}
