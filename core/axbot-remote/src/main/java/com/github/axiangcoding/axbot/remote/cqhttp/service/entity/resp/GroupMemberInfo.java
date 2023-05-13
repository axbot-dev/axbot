package com.github.axiangcoding.axbot.remote.cqhttp.service.entity.resp;

import lombok.Data;

/**
 * 描述于 <a href="https://docs.go-cqhttp.org/api/#%E8%8E%B7%E5%8F%96%E7%BE%A4%E6%88%90%E5%91%98%E4%BF%A1%E6%81%AF">文档</a>
 */
@Data
public class GroupMemberInfo {
    Long groupId;
    Long userId;
    String nickname;
    String card;
    String sex;
    Integer age;
    String area;
    Long joinTime;
    String level;
    String role;

    Boolean unfriendly;

}
