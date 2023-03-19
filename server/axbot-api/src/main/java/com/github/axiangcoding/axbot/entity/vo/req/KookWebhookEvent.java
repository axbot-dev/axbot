package com.github.axiangcoding.axbot.entity.vo.req;

import com.github.axiangcoding.axbot.kook.entity.KookEvent;
import lombok.Data;

@Data
public class KookWebhookEvent {
    KookEvent d;
    String s;
}
