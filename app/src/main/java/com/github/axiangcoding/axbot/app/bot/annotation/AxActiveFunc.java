package com.github.axiangcoding.axbot.app.bot.annotation;

import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface AxActiveFunc {
    ActiveEvent event();
}
