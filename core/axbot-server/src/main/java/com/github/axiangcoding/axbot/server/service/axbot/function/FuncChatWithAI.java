package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.service.AIService;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.TextCensorService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class FuncChatWithAI extends InteractiveFunction {
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
