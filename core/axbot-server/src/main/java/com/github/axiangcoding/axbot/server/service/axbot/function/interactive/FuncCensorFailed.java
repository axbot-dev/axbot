package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.UserInputRecordService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class FuncCensorFailed extends InteractiveFunction {
    @Resource
    UserInputRecordService userInputRecordService;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Resource
    FuncUserBanned funcUserBanned;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        long counted = userInputRecordService.countUserKookSensitiveInput(input.getUserId());
        long limit = 5;
        if (counted > limit) {
            kookUserSettingService.blockUser(input.getUserId(), "输入5次以上不合时宜的命令");
            return funcUserBanned.execute(input);
        }

        KookQuickCard card = new KookQuickCard("AXBot不会响应这条消息", "danger");
        card.addModule(KookCardMessage.quickMdSection("本条消息的输入被AI判断为不合时宜，不会做出任何响应"));
        card.addModule(KookCardMessage.quickMdSection("累计5条消息被判定为不合时宜，你会被拉黑"));
        card.addModule(KookCardMessage.quickMdSection("你已输入 %s / %s 条不合时宜的消息".formatted(
                KookMDMessage.code(String.valueOf(counted)), KookMDMessage.code(String.valueOf(limit)))));
        card.addModule(KookCardMessage.newDivider());
        card.addModule(KookCardMessage.quickMdSection("注意：如果你的ID是纯数字，请使用“查找”，这样可以减少误判几率"));
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {

        long counted = userInputRecordService.countUserCqhttpSensitiveInput(input.getUserId());
        long limit = 5;
        if (counted > limit) {
            qUserSettingService.blockUser(input.getUserId(), "输入5次以上不合时宜的命令");
            return funcUserBanned.execute(input);
        }

        CqhttpQuickMsg msg = new CqhttpQuickMsg("AXBot不会响应这条消息");
        msg.addLine("本条消息的输入被AI判断为不合时宜，不会做出任何响应");
        msg.addLine("累计5条消息被判定为不合时宜，你会被拉黑");
        msg.addLine("你已输入 %d / %d 条不合时宜的消息".formatted(counted, (limit)));
        msg.addDivider();
        msg.addLine("注意：如果你的ID是纯数字，请使用“查找”，这样可以减少误判几率");
        return input.response(msg.displayWithFooter());
    }
}
