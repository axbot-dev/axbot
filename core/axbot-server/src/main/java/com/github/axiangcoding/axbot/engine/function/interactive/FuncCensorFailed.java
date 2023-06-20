package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.SupportPlatform;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.UserInputRecordService;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@AxbotInteractiveFunc(command = InteractiveCommand.CENSOR_FAILED)
@Component
public class FuncCensorFailed extends AbstractInteractiveFunction {
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
        long counted = userInputRecordService.countLatestSensitiveInput(input.getUserId(), SupportPlatform.KOOK);
        long limit = 5;
        if (counted > limit) {
            kookUserSettingService.blockUser(input.getUserId(), "输入5次以上违规的命令");
            return funcUserBanned.execute(input);
        }
        KookQuickCard card = new KookQuickCard("AXBot不会响应这条消息", "danger");
        card.addModuleMdSection("本条消息的输入被AI判定为违规，因此机器人不会做出任何正常回复");
        card.addModuleMdSection("最近一个月内如果累计5条消息被判定为违规，你将会被AXBot拉黑");
        card.addModuleMdSection("当前你已达到 %s / %s".formatted(
                KookMDMessage.code(String.valueOf(counted)), KookMDMessage.code(String.valueOf(limit))));
        card.addModuleMdSection("请注意：AXBot不对AI审核的结果负责");
        card.addModuleDivider();
        card.addModuleGetHelp("如果你被误判拉黑，可以到服务器中的“#拉黑申述”频道申请解封");
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        long counted = userInputRecordService.countLatestSensitiveInput(input.getUserId(), SupportPlatform.CQHTTP);
        long limit = 5;
        if (counted > limit) {
            qUserSettingService.blockUser(input.getUserId(), "输入5次以上不合时宜的命令");
            return funcUserBanned.execute(input);
        }

        CqhttpQuickMsg msg = new CqhttpQuickMsg("AXBot不会响应这条消息");
        msg.addLine("本条消息的输入被AI判定为违规，因此机器人不会做出任何正常回复");
        msg.addLine("最近一个月内如果累计5条消息被判定为违规，你将会被AXBot拉黑");
        msg.addLine("当前你已达到 %d / %d".formatted(counted, limit));
        msg.addLine("请注意：AXBot不对AI审核的结果负责");
        msg.addDivider();
        msg.addLine("如果你被误判拉黑，请到KOOK频道申请解封");
        return input.response(msg.display());
    }
}
