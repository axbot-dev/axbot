package com.github.axiangcoding.axbot.server.service.axbot;

import com.github.axiangcoding.axbot.server.service.axbot.function.*;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FunctionRegister {
    @Resource
    FuncDefault funcDefault;
    @Resource
    FuncHelp funcHelp;
    @Resource
    FuncVersion funcVersion;
    @Resource
    FuncLuckyToday funcLuckyToday;
    @Resource
    FuncWtQueryProfile funcWtQueryProfile;
    @Resource
    FuncWtUpdateProfile funcWtUpdateProfile;
    @Resource
    FuncGuildStatus funcGuildStatus;
    @Resource
    FuncUserStatus funcUserStatus;
    @Resource
    FuncManageGuild funcManageGuild;
    @Resource
    FuncChatWithAI funcChatWithAI;

    @Resource
    FuncGuildBanned funcGuildBanned;
    @Resource
    FuncUserBanned funcUserBanned;
    @Resource
    FuncUsageLimit funcUsageLimit;
    @Resource
    FuncCensorFailed funcCensorFailed;
}
