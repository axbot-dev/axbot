package com.github.axiangcoding.axbot.server.service;


import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.v1.NotificationEvent;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.function.NotificationFunction;
import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.NotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserUsage;
import com.github.axiangcoding.axbot.server.service.axbot.FunctionRegister;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BotService {
    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    FunctionRegister functionRegister;
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

    public boolean isPlatformEnabled(SupportPlatform platform) {
        if (platform == SupportPlatform.PLATFORM_KOOK && botConfProps.getKook().getEnabled()) {
            return true;
        } else if (platform == SupportPlatform.PLATFORM_CQHTTP && botConfProps.getCqhttp().getEnabled()) {
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
        if (platform == SupportPlatform.PLATFORM_KOOK) {
            KookInteractiveOutput output = processKookInteractiveFunction((KookInteractiveInput) input);
            if (output == null) {
                return;
            }
            remoteClientService.sendKookCardMsg(output);
        } else if (platform == SupportPlatform.PLATFORM_CQHTTP) {
            CqhttpInteractiveOutput output = processCqhttpInteractiveFunction((CqhttpInteractiveInput) input);
            if (output == null) {
                return;
            }
            remoteClientService.sendCqhttpMsg(output);
        }

    }

    public void responseForNotification(SupportPlatform platform, NotificationInput input) {
        if (platform == SupportPlatform.PLATFORM_KOOK) {
            KookNotificationOutput output = processKookNotificationFunction((KookNotificationInput) input);
            if (output == null) {
                return;
            }
            remoteClientService.sendKookCardMsg(output);
        } else if (platform == SupportPlatform.PLATFORM_CQHTTP) {
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
        String userId = input.getUserId();
        String guildId = input.getGuildId();
        String channelId = input.getChannelId();
        String message = StringUtils.join(input.getParamList(), " ");
        // 获取服务器和用户设置，如果不存在则创建一个新的
        KookGuildSetting guildSetting = kookGuildSettingService.getOrDefault(guildId);
        KookUserSetting userSetting = kookUserSettingService.getOrDefault(userId);

        // 记录输入内容
        long inputId = userInputRecordService.saveRecordFromKook(userId, message, input.getGuildId(), channelId);
        kookUserSettingService.updateInputUsage(userSetting);
        input.setInputId(inputId);
        // 检查是否被禁用
        if (guildSetting.getBanned()) {
            return functionRegister.getFuncGuildBanned().execute(input);
        }
        if (userSetting.getBanned()) {
            return functionRegister.getFuncUserBanned().execute(input);
        }

        // 检查使用次数是否超限
        UserUsage usage = userSetting.getUsage();
        int inputLimit = kookUserSettingService.getInputLimit(userId);
        if (usage.getInputToday() > inputLimit) {
            return functionRegister.getFuncUsageLimit().execute(input);
        }
        // 文本AI检查
        boolean textPassCheck = textCensorService.checkAndCacheText(message);
        if (!textPassCheck) {
            userInputRecordService.updateSensitive(inputId, true);
            return functionRegister.getFuncCensorFailed().execute(input);
        }

        InteractiveCommand command = input.getCommand();
        InteractiveFunction function;
        switch (command) {
            case COMMAND_DEFAULT -> function = functionRegister.getFuncDefault();
            case COMMAND_HELP -> function = functionRegister.getFuncHelp();
            case COMMAND_VERSION -> function = functionRegister.getFuncVersion();
            case COMMAND_LUCKY -> function = functionRegister.getFuncLuckyToday();
            case COMMAND_WT_QUERY_PROFILE -> function = functionRegister.getFuncWtQueryProfile();
            case COMMAND_WT_UPDATE_PROFILE -> function = functionRegister.getFuncWtUpdateProfile();
            case COMMAND_GUILD_STATUS -> function = functionRegister.getFuncGuildStatus();
            case COMMAND_USER_STATUS -> function = functionRegister.getFuncUserStatus();
            case COMMAND_GUILD_MANAGE -> function = functionRegister.getFuncManageGuild();
            case COMMAND_CHAT_WITH_AI -> function = functionRegister.getFuncChatWithAI();
            case COMMAND_SPONSOR -> function = functionRegister.getFuncSponsor();
            default -> function = functionRegister.getFuncDefault();
        }
        return function.execute(input);
    }

    public CqhttpInteractiveOutput processCqhttpInteractiveFunction(CqhttpInteractiveInput input) {
        // 记录输入内容
        String message = StringUtils.join(input.getParamList(), " ");
        String groupId = input.getGroupId();
        String userId = input.getUserId();
        // 获取服务器和用户设置，如果不存在则创建一个新的
        QGroupSetting groupSetting = qGroupSettingService.getOrDefault(groupId);
        QUserSetting userSetting = qUserSettingService.getOrDefault(userId);

        long inputId = userInputRecordService.saveRecordFromCqhttp(userId, message, groupId);
        qUserSettingService.updateInputUsage(userSetting);
        input.setInputId(inputId);
        // 检查是否被禁用
        if (groupSetting.getBanned()) {
            return functionRegister.getFuncGuildBanned().execute(input);
        }
        if (userSetting.getBanned()) {
            return functionRegister.getFuncUserBanned().execute(input);
        }

        // 检查使用次数是否超限
        UserUsage usage = userSetting.getUsage();
        int inputLimit = qUserSettingService.getInputLimit(userId);
        if (usage.getInputToday() > inputLimit) {
            return functionRegister.getFuncUsageLimit().execute(input);
        }
        // 文本AI检查
        boolean textPassCheck = textCensorService.checkAndCacheText(message);
        if (!textPassCheck) {
            userInputRecordService.updateSensitive(inputId, true);
            return functionRegister.getFuncCensorFailed().execute(input);
        }

        InteractiveCommand command = input.getCommand();
        InteractiveFunction function;
        switch (command) {
            case COMMAND_DEFAULT -> function = functionRegister.getFuncDefault();
            case COMMAND_HELP -> function = functionRegister.getFuncHelp();
            case COMMAND_VERSION -> function = functionRegister.getFuncVersion();
            case COMMAND_LUCKY -> function = functionRegister.getFuncLuckyToday();
            case COMMAND_WT_QUERY_PROFILE -> function = functionRegister.getFuncWtQueryProfile();
            case COMMAND_WT_UPDATE_PROFILE -> function = functionRegister.getFuncWtUpdateProfile();
            case COMMAND_GUILD_STATUS -> function = functionRegister.getFuncGuildStatus();
            case COMMAND_USER_STATUS -> function = functionRegister.getFuncUserStatus();
            case COMMAND_GUILD_MANAGE -> function = functionRegister.getFuncManageGuild();
            case COMMAND_CHAT_WITH_AI -> function = functionRegister.getFuncChatWithAI();
            case COMMAND_SPONSOR -> function = functionRegister.getFuncSponsor();
            default -> function = functionRegister.getFuncDefault();
        }
        return function.execute(input);
    }


    private KookNotificationOutput processKookNotificationFunction(KookNotificationInput input) {
        NotificationEvent event = input.getEvent();
        NotificationFunction function;
        switch (event) {
            case EVENT_JOIN_GUILD -> function = functionRegister.getFuncJoinGuild();
            case EVENT_EXIT_GUILD -> function = functionRegister.getFuncExitGuild();
            case EVENT_BILI_ROOM_REMIND -> function = functionRegister.getFuncBiliRoomRemind();
            case EVENT_WT_NEWS -> function = functionRegister.getFuncWTNews();
            default -> {
                log.warn("no such notification event: {}", event);
                return null;
            }
        }
        return function.execute(input);
    }

    private CqhttpNotificationOutput processCqhttpNotificationFunction(CqhttpNotificationInput input) {
        NotificationEvent event = input.getEvent();
        NotificationFunction function;
        switch (event) {
            case EVENT_JOIN_GUILD -> function = functionRegister.getFuncJoinGuild();
            case EVENT_EXIT_GUILD -> function = functionRegister.getFuncExitGuild();
            case EVENT_BILI_ROOM_REMIND -> function = functionRegister.getFuncBiliRoomRemind();
            case EVENT_WT_NEWS -> function = functionRegister.getFuncWTNews();
            default -> {
                log.warn("no such notification event: {}", event);
                return null;
            }

        }
        return function.execute(input);
    }


}