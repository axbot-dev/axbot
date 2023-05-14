package com.github.axiangcoding.axbot.server.service.axbot;

import com.github.axiangcoding.axbot.server.service.axbot.function.interactive.*;
import com.github.axiangcoding.axbot.server.service.axbot.function.notification.FuncBiliRoomRemind;
import com.github.axiangcoding.axbot.server.service.axbot.function.notification.FuncExitGuild;
import com.github.axiangcoding.axbot.server.service.axbot.function.notification.FuncJoinGuild;
import com.github.axiangcoding.axbot.server.service.axbot.function.notification.FuncWTNews;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FunctionRegister {
    // 交互类
    @Resource
    FuncDefault funcDefault;
    @Resource
    FuncError funcError;
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
    FuncSponsor funcSponsor;

    @Resource
    FuncGuildBanned funcGuildBanned;
    @Resource
    FuncUserBanned funcUserBanned;
    @Resource
    FuncUsageLimit funcUsageLimit;
    @Resource
    FuncCensorFailed funcCensorFailed;

    // 通知类
    @Resource
    FuncJoinGuild funcJoinGuild;
    @Resource
    FuncExitGuild funcExitGuild;
    @Resource
    FuncBiliRoomRemind funcBiliRoomRemind;
    @Resource
    FuncWTNews funcWTNews;
}
