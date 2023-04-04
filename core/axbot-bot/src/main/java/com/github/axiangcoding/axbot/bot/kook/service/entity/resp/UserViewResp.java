package com.github.axiangcoding.axbot.bot.kook.service.entity.resp;

import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonResp;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserViewResp extends CommonResp {
    CommonUser data;
}
