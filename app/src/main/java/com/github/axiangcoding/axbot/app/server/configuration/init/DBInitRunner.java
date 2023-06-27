package com.github.axiangcoding.axbot.app.server.configuration.init;

import com.github.axiangcoding.axbot.app.server.service.AppUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DBInitRunner implements ApplicationRunner {
    @Resource
    AppUserService appUserService;

    @Override
    public void run(ApplicationArguments args) {
        long count = appUserService.count();
        if (count == 0) {
            appUserService.initSuperAdminUser();
        }
    }


}
