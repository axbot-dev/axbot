package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.UserInputRecord;
import com.github.axiangcoding.axbot.server.service.UserInputRecordService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FuncWtQueryHistory extends InteractiveFunction {
    @Resource
    UserInputRecordService userInputRecordService;

    @Resource
    BotConfProps botConfProps;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard card = new KookQuickCard("战争雷霆查询的历史记录", "success");
        List<String> list = genList(input.getUserId(), SupportPlatform.PLATFORM_KOOK);
        for (String item : list) {
            card.addModule(KookCardMessage.quickMdSection(KookMDMessage.code(item)));
        }
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("战争雷霆查询的历史记录");
        List<String> list = genList(input.getUserId(), SupportPlatform.PLATFORM_CQHTTP);
        for (String item : list) {
            msg.addLine(item);
        }
        return input.response(msg.displayWithFooter());
    }


    private List<String> genList(String userId, SupportPlatform platform) {
        List<UserInputRecord> wtQueryHistory = userInputRecordService.findWtQueryHistory(userId, platform.getLabel());
        List<String> list = new ArrayList<>();

        String prefix = botConfProps.getTriggerMessagePrefix().get(0);
        for (UserInputRecord record : wtQueryHistory) {
            if (Boolean.TRUE.equals(record.getSensitive())) {
                list.add("这是一个不合时宜的命令，不予显示");
            } else {
                list.add("%s %s %s".formatted(prefix, InteractiveCommand.WT_QUERY_PROFILE.toCommand(), record.getInput()));
            }
        }
        return list;
    }
}
