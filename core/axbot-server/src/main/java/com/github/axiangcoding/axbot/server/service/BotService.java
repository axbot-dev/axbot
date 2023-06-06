package com.github.axiangcoding.axbot.server.service;


import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.NotificationEvent;
import com.github.axiangcoding.axbot.engine.SupportPlatform;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.annot.AxbotNotificationFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.function.AbstractNotificationFunction;
import com.github.axiangcoding.axbot.engine.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.io.NotificationInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserUsage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class BotService {
    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    RemoteClientService remoteClientService;
    @Resource
    TextCensorService textCensorService;
    @Resource
    KookGuildSettingService kookGuildSettingService;
    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QGroupSettingService qGroupSettingService;
    @Resource
    QUserSettingService qUserSettingService;
    @Resource
    UserInputRecordService userInputRecordService;

    @Resource
    BotConfProps botConfProps;

    @Resource
    ApplicationContext applicationContext;

    public boolean isPlatformEnabled(SupportPlatform platform) {
        if (platform == SupportPlatform.KOOK && botConfProps.getKook().getEnabled()) {
            return true;
        } else if (platform == SupportPlatform.CQHTTP && botConfProps.getCqhttp().getEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 同步响应用户输入
     *
     * @param platform
     * @param input
     * @return
     */
    public void responseForInteractive(SupportPlatform platform, InteractiveInput input) {
        if (platform == SupportPlatform.KOOK) {
            KookInteractiveOutput output = processKookInteractiveFunction((KookInteractiveInput) input);
            if (output == null) {
                return;
            }
            remoteClientService.sendKookCardMsg(output);
        } else if (platform == SupportPlatform.CQHTTP) {
            CqhttpInteractiveOutput output = processCqhttpInteractiveFunction((CqhttpInteractiveInput) input);
            if (output == null) {
                return;
            }
            remoteClientService.sendCqhttpMsg(output);
        }

    }

    public void responseForNotification(SupportPlatform platform, NotificationInput input) {
        if (platform == SupportPlatform.KOOK) {
            KookNotificationOutput output = processKookNotificationFunction((KookNotificationInput) input);
            if (output == null) {
                return;
            }
            remoteClientService.sendKookCardMsg(output);
        } else if (platform == SupportPlatform.CQHTTP) {
            CqhttpNotificationOutput output = processCqhttpNotificationFunction((CqhttpNotificationInput) input);
            if (output == null) {
                return;
            }
            remoteClientService.sendCqhttpMsg(output);
        }
    }

    /**
     * 异步响应用户输入
     *
     * @param input
     * @return
     */
    public void responseForInteractiveAsync(SupportPlatform platform, InteractiveInput input) {
        threadPoolTaskExecutor.execute(() -> {
            try {
                responseForInteractive(platform, input);
            } catch (Exception e) {
                log.error("generate response for interactive async error", e);
            }
        });
    }

    /**
     * 异步响通知输入
     *
     * @param input
     * @return
     */
    public void responseForNotificationAsync(SupportPlatform platform, NotificationInput input) {
        threadPoolTaskExecutor.execute(() -> {
            try {
                responseForNotification(platform, input);
            } catch (Exception e) {
                log.error("generate response for notification async error", e);
            }
        });
    }


    public KookInteractiveOutput processKookInteractiveFunction(KookInteractiveInput input) {
        try {
            String userId = input.getUserId();
            String guildId = input.getGuildId();
            String channelId = input.getChannelId();
            String message = StringUtils.join(input.getParamList(), " ");
            // 获取服务器和用户设置，如果不存在则创建一个新的
            KookGuildSetting guildSetting = kookGuildSettingService.getOrDefault(guildId);
            KookUserSetting userSetting = kookUserSettingService.getOrDefault(userId);

            // 记录输入内容
            long inputId = userInputRecordService.saveRecordFromKook(userId, input.getCommand(), message, input.getGuildId(), channelId);
            kookUserSettingService.updateInputUsage(userSetting);
            input.setInputId(inputId);
            // 检查是否被禁用
            if (guildSetting.getBanned()) {
                return findInteractiveFunc(InteractiveCommand.GUILD_BANNED).execute(input);
            }
            if (userSetting.getBanned()) {
                return findInteractiveFunc(InteractiveCommand.USER_BANNED).execute(input);
            }

            // 检查使用次数是否超限
            UserUsage usage = userSetting.getUsage();
            int inputLimit = kookUserSettingService.getInputLimit(userId);
            if (usage.getInputToday() > inputLimit) {
                return findInteractiveFunc(InteractiveCommand.USAGE_LIMIT).execute(input);
            }
            // 文本AI检查
            boolean textPassCheck = textCensorService.checkAndCacheText(message);
            if (!textPassCheck) {
                userInputRecordService.updateSensitive(inputId, true);
                return findInteractiveFunc(InteractiveCommand.CENSOR_FAILED).execute(input);
            }
            InteractiveCommand command = input.getCommand();
            return findInteractiveFunc(command).execute(input);
        } catch (RuntimeException e) {
            log.error("process kook interactive function error", e);
            return findInteractiveFunc(InteractiveCommand.ERROR).execute(input);
        }

    }

    public CqhttpInteractiveOutput processCqhttpInteractiveFunction(CqhttpInteractiveInput input) {
        try {
            // 记录输入内容
            String message = StringUtils.join(input.getParamList(), " ");
            String groupId = input.getGroupId();
            String userId = input.getUserId();
            // 获取服务器和用户设置，如果不存在则创建一个新的
            QGroupSetting groupSetting = qGroupSettingService.getOrDefault(groupId);
            QUserSetting userSetting = qUserSettingService.getOrDefault(userId);

            long inputId = userInputRecordService.saveRecordFromCqhttp(userId, input.getCommand(), message, groupId);
            qUserSettingService.updateInputUsage(userSetting);
            input.setInputId(inputId);
            // 检查是否被禁用
            if (groupSetting.getBanned()) {
                return findInteractiveFunc(InteractiveCommand.GUILD_BANNED).execute(input);
            }
            if (userSetting.getBanned()) {
                return findInteractiveFunc(InteractiveCommand.USER_BANNED).execute(input);
            }

            // 检查使用次数是否超限
            UserUsage usage = userSetting.getUsage();
            int inputLimit = qUserSettingService.getInputLimit(userId);
            if (usage.getInputToday() > inputLimit) {
                return findInteractiveFunc(InteractiveCommand.USAGE_LIMIT).execute(input);
            }
            // 文本AI检查
            boolean textPassCheck = textCensorService.checkAndCacheText(message);
            if (!textPassCheck) {
                userInputRecordService.updateSensitive(inputId, true);
                return findInteractiveFunc(InteractiveCommand.CENSOR_FAILED).execute(input);
            }
            InteractiveCommand command = input.getCommand();
            return findInteractiveFunc(command).execute(input);
        } catch (RuntimeException e) {
            log.error("process cqhttp interactive function error", e);
            return findInteractiveFunc(InteractiveCommand.ERROR).execute(input);
        }
    }


    private KookNotificationOutput processKookNotificationFunction(KookNotificationInput input) {
        NotificationEvent event = input.getEvent();
        return findNotificationFunc(event).execute(input);
    }

    private CqhttpNotificationOutput processCqhttpNotificationFunction(CqhttpNotificationInput input) {
        NotificationEvent event = input.getEvent();
        return findNotificationFunc(event).execute(input);
    }

    private AbstractInteractiveFunction findInteractiveFunc(InteractiveCommand command) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(AxbotInteractiveFunc.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            AbstractInteractiveFunction func = applicationContext.getBean(entry.getKey(), AbstractInteractiveFunction.class);
            AxbotInteractiveFunc annotation = func.getClass().getAnnotation(AxbotInteractiveFunc.class);
            if (annotation.command().equals(command)) {
                return func;
            }
        }
        throw new BusinessException(CommonError.ERROR, "no such interactive function: " + command.toCommand());
    }

    private AbstractNotificationFunction findNotificationFunc(NotificationEvent event) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(AxbotNotificationFunc.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            AbstractNotificationFunction func = applicationContext.getBean(entry.getKey(), AbstractNotificationFunction.class);
            AxbotNotificationFunc annotation = func.getClass().getAnnotation(AxbotNotificationFunc.class);
            if (annotation.event().equals(event)) {
                return func;
            }
        }
        throw new BusinessException(CommonError.ERROR, "no such notification function: " + event.name());
    }
}
