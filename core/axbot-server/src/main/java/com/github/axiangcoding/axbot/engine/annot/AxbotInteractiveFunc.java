package com.github.axiangcoding.axbot.engine.annot;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AxbotInteractiveFunc {
    String description() default "交互式命令";

    InteractiveCommand command();
}
