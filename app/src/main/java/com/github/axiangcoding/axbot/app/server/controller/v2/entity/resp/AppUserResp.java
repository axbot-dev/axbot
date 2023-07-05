package com.github.axiangcoding.axbot.app.server.controller.v2.entity.resp;

import com.github.axiangcoding.axbot.app.server.data.entity.AppUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUserResp {
    String userId;
    String username;
    LocalDateTime createTime;
    LocalDateTime lastLoginTime;

    Integer loginFailed;
    LocalDateTime lastLoginFailedTime;
    String status;
    Boolean isSuperAdmin;

    public static AppUserResp from(AppUser user) {
        AppUserResp vo = new AppUserResp();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setCreateTime(user.getCreateTime());
        vo.setLastLoginTime(user.getLastLoginTime());
        vo.setLoginFailed(user.getLoginFailed());
        vo.setLastLoginFailedTime(user.getLastLoginFailedTime());
        vo.setStatus(user.getStatus());
        vo.setIsSuperAdmin(user.getIsSuperAdmin());
        return vo;
    }
}
