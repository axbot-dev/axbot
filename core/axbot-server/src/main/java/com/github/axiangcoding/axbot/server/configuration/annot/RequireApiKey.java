package com.github.axiangcoding.axbot.server.configuration.annot;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SecurityRequirement(name = "api-key")
public @interface RequireApiKey {
}
