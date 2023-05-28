package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.RemoteClientService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FuncBugReport extends AbstractInteractiveFunction {
    @Resource
    RemoteClientService remoteClientService;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        List<KookUserSetting> list = kookUserSettingService.getKookSuperAdminUser();
        list.forEach(item -> {
            String content = userReportDetail(input.getUserId(), input.getGuildId(), StringUtils.join(input.getParamList(), " "));
            remoteClientService.sendKookPrivateCardMsg(item.getUserId(),
                    content);
        });

        return input.response(bugReportSuccess());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        return input.response(CqhttpQuickMsg.notSupport().displayWithFooter());
    }

    private String bugReportSuccess() {
        KookQuickCard card = new KookQuickCard("BUG报告成功", "success");
        card.addModuleMdSection("感谢您的反馈，超管用户已经收到了bot的私信，将会酌情安排处理。");
        return card.displayWithFooter();
    }

    private String userReportDetail(String userId, String guildId, String content) {
        KookQuickCard card = new KookQuickCard("你好超管，收到了一条用户反馈", "info");
        card.addModuleMdSection("用户ID：" + userId);
        card.addModuleMdSection("服务器ID：" + guildId);
        card.addModuleMdSection("反馈内容：" + content);
        return card.displayWithFooter();
    }
}
