package com.github.axiangcoding.axbot.engine.annot;

import com.github.axiangcoding.axbot.engine.NotificationEvent;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AxbotNotificationFunc {
    String description() default "通知事件";

    NotificationEvent event();
}
