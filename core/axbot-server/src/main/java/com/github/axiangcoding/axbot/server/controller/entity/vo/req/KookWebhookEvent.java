package com.github.axiangcoding.axbot.server.controller.entity.vo.req;


import com.github.axiangcoding.axbot.remote.kook.entity.KookEvent;
import lombok.Data;

@Data
public class KookWebhookEvent {
    KookEvent d;
    String s;
}
