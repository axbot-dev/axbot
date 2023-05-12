package com.github.axiangcoding.axbot.server.service;


import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.service.axbot.FunctionRegister;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 同步响应用户输入
     *
     * @param platform
     * @param input
     * @return
     */
    public void responseForInteractive(SupportPlatform platform, InteractiveInput input) {
        if (platform == SupportPlatform.PLATFORM_KOOK) {
            KookInteractiveOutput output = processKookUserEvent((KookInteractiveInput) input);
            remoteClientService.sendKookCardMsg(output);
        } else if (platform == SupportPlatform.PLATFORM_CQHTTP) {

            CqhttpInteractiveOutput output = processCqhttpUserEvent((CqhttpInteractiveInput) input);
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
                log.error("generate response for user input async error", e);
            }
        });
    }

    public KookInteractiveOutput processKookUserEvent(KookInteractiveInput input) {
        // FIXME 检查群组和用户是否被拉黑
        // FIXME 检查使用情况是否超限
        // FIXME 新增使用次数
        // FIXME 文本检查
        String guildId = input.getGuildId();
        KookGuildSetting guildSetting = kookGuildSettingService.getOrDefault(guildId);
        if (guildSetting.getBanned()) {
            return functionRegister.getFuncGuildBanned().execute(input);
        }
        String userId = input.getUserId();
        KookUserSetting userSetting = kookUserSettingService.getOrDefault(userId);
        if (userSetting.getBanned()) {
            return functionRegister.getFuncUserBanned().execute(input);
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
            default -> function = functionRegister.getFuncDefault();
        }
        return function.execute(input);
    }

    public CqhttpInteractiveOutput processCqhttpUserEvent(CqhttpInteractiveInput input) {
        // FIXME 检查群组和用户是否被拉黑
        // FIXME 检查使用情况是否超限
        // FIXME 新增使用次数
        // FIXME 文本检查
        String groupId = input.getGroupId();
        QGroupSetting groupSetting = qGroupSettingService.getOrDefault(groupId);
        if (groupSetting.getBanned()) {
            return functionRegister.getFuncGuildBanned().execute(input);
        }
        String userId = input.getUserId();
        QUserSetting userSetting = qUserSettingService.getOrDefault(userId);
        if (userSetting.getBanned()) {
            return functionRegister.getFuncUserBanned().execute(input);
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
            default -> function = functionRegister.getFuncDefault();
        }
        return function.execute(input);
    }
}
