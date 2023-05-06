package com.github.axiangcoding.axbot.server.controller.entity.vo.resp;

import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GlobalUserVo {
    String userId;
    String username;
    LocalDateTime createTime;
    LocalDateTime lastLoginTime;
    Boolean isAdmin;

    public static GlobalUserVo from(GlobalUser user) {
        GlobalUserVo vo = new GlobalUserVo();
        vo.setUserId(user.getUserId().toString());
        vo.setUsername(user.getUsername());
        vo.setCreateTime(user.getCreateTime());
        vo.setLastLoginTime(user.getLastLoginTime());
        vo.setIsAdmin(user.getIsAdmin());
        return vo;
    }
}
