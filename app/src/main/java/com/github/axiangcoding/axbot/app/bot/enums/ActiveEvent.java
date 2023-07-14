package com.github.axiangcoding.axbot.app.bot.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ActiveEvent {
    /*
    以下是系统类通知
     */
    JOIN_GUILD,

    EXIT_GUILD,
    REPORT_TRACE,
    GET_ROLE,

    /*
     * 以下是游戏类通知
     */
    BILI_ROOM_REMIND,

    REMIND_WT_NEWS,

    SEND_WT_TEXT_PROFILE,
}
