package com.github.axiangcoding.axbot.bot.kook.service.entity.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * link to <a href="https://developer.kookapp.cn/doc/http/message#%E5%8F%91%E9%80%81%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF">...</a>
 */
@Getter
@Setter
@ToString
public class CreateMessageReq {
    Integer type;
    String targetId;
    String content;
    String quote;
    String nonce;
    String tempTargetId;

}
