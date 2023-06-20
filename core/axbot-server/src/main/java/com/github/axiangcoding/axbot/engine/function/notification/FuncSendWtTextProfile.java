package com.github.axiangcoding.axbot.engine.function.notification;

import com.github.axiangcoding.axbot.engine.NotificationEvent;
import com.github.axiangcoding.axbot.engine.annot.AxbotNotificationFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractNotificationFunction;
import com.github.axiangcoding.axbot.engine.function.common.CommonWtProfile;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.service.RemoteClientService;
import com.github.axiangcoding.axbot.server.service.WTGamerProfileService;
import com.github.axiangcoding.axbot.server.service.entity.SendTextWtProfile;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AxbotNotificationFunc(event = NotificationEvent.SEND_WT_TEXT_PROFILE)
@Component
public class FuncSendWtTextProfile extends AbstractNotificationFunction {
    @Resource
    RemoteClientService remoteClientService;


    @Resource
    CommonWtProfile commonWtProfile;

    @Resource
    WTGamerProfileService wtGamerProfileService;

    @Override
    public KookNotificationOutput execute(KookNotificationInput input) {
        SendTextWtProfile data = ((SendTextWtProfile) input.getData());
        String nickname = data.getText();
        Optional<WtGamerProfile> opt = wtGamerProfileService.findByNickname(nickname);
        if (opt.isEmpty()) {
            remoteClientService.sendKookPrivateTextMsg(data.getUserId(), "对不起，该玩家的记录已经消失");
            return null;
        }
        String text = commonWtProfile.cqhttpProfileFound(nickname, opt.get(), '=');

        remoteClientService.sendKookPrivateTextMsg(data.getUserId(), text);
        return null;
    }

    @Override
    public CqhttpNotificationOutput execute(CqhttpNotificationInput input) {
        throw new BusinessException(CommonError.NOT_SUPPORT, "cqhttp not support this function");
    }
}
