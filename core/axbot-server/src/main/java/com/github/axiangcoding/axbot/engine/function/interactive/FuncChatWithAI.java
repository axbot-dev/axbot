package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.service.AIService;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.TextCensorService;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@AxbotInteractiveFunc(command = InteractiveCommand.CHAT_WITH_AI)
@Component
public class FuncChatWithAI extends AbstractInteractiveFunction {
    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Resource
    AIService aiService;

    @Resource
    TextCensorService textCensorService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String userId = input.getUserId();
        KookQuickCard card = new KookQuickCard("和AXBot聊天", "success");

        if (kookUserSettingService.canUseAI(userId)) {
            String chat = aiService.singleChat(getAsk(input));
            if (textCensorService.checkText(chat)) {
                card.addModule(KookCardMessage.quickMdSection(chat));
            } else {
                card.addModule(KookCardMessage.quickMdSection("对不起，AI的回答中带有不合时宜的内容，不予展示"));
            }
        } else {
            card.addModule(KookCardMessage.quickMdSection("对不起，你没有使用AI能力的权限"));
        }
        return input.response(card.displayWithFooter());

    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        String userId = input.getUserId();
        CqhttpQuickMsg msg = new CqhttpQuickMsg("和AXBot聊天");
        if (qUserSettingService.canUseAI(userId)) {
            String chat = aiService.singleChat(getAsk(input));
            if (textCensorService.checkText(chat)) {
                msg.addLine(chat);
            } else {
                msg.addLine("对不起，AI的回答中带有不合时宜的内容，不予展示");
            }
        } else {
            msg.addLine("对不起，你没有使用AI能力的权限");
        }
        return input.response(msg.displayWithFooter());
    }

    private String getAsk(InteractiveInput input) {
        String[] paramList = input.getParamList();
        return StringUtils.join(paramList, " ");
    }
}
