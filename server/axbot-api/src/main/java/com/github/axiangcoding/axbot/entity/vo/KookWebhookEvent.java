package com.github.axiangcoding.axbot.entity.vo;

import com.github.axiangcoding.axbot.kook.entity.KookEvent;
import lombok.Data;

@Data
public class KookWebhookEvent {
    KookEvent d;
    String s;
}
