package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.RemoteClientService;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@AxbotInteractiveFunc(command = InteractiveCommand.BUG_REPORT)
@Component
public class FuncBugReport extends AbstractInteractiveFunction {
    @Resource
    RemoteClientService remoteClientService;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String detail = StringUtils.join(input.getParamList(), " ");
        if (StringUtils.isBlank(detail)) {
            return input.response(noContent());
        }

        List<KookUserSetting> list = kookUserSettingService.getSuperAdminUser();
        list.forEach(item -> {
            String content = userReportDetail(input.getUserId(), input.getGuildId(), detail);
            remoteClientService.sendKookPrivateCardMsg(item.getUserId(),
                    content);
        });

        return input.response(bugReportSuccess());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        return input.response(CqhttpQuickMsg.notSupport("请到KOOK频道上反馈问题").display());
    }

    private String bugReportSuccess() {
        KookQuickCard card = new KookQuickCard("BUG报告成功", "success");
        card.addModuleMdSection("感谢您的反馈，超管用户已经收到了bot的私信，将会安排处理。");
        card.addModuleGetHelp("你也可以到“#问题反馈和意见建议”频道直接留言反馈问题");
        return card.displayWithFooter();
    }

    private String noContent() {
        KookQuickCard card = new KookQuickCard("BUG报告失败", "warning");
        card.addModuleMdSection("BUG报告内容不能为空");
        return card.displayWithFooter();
    }

    private String userReportDetail(String userId, String guildId, String content) {
        KookQuickCard card = new KookQuickCard("你好超管用户，收到了一条用户反馈", "info");
        card.addModuleMdSection("用户ID：" + userId);
        card.addModuleMdSection("服务器ID：" + guildId);
        card.addModuleMdSection("反馈内容：" + content);
        return card.displayWithFooter();
    }
}
