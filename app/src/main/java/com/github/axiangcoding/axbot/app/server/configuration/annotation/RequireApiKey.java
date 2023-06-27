package com.github.axiangcoding.axbot.app.server.configuration.annotation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SecurityRequirement(name = "api-key")
public @interface RequireApiKey {
    boolean admin() default false;
}
