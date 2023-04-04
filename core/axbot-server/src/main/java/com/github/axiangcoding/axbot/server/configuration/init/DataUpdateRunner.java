package com.github.axiangcoding.axbot.server.configuration.init;

import com.github.axiangcoding.axbot.server.data.entity.GlobalSetting;
import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
import com.github.axiangcoding.axbot.server.data.repository.GlobalSettingRepository;
import com.github.axiangcoding.axbot.server.data.repository.GlobalUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class DataUpdateRunner implements ApplicationRunner {
    @Resource
    GlobalSettingRepository globalSettingRepository;

    @Resource
    GlobalUserRepository globalUserRepository;

    @Override
    public void run(ApplicationArguments args) {
        update0();
        update1();
    }

    private void update0() {
        Optional<GlobalSetting> opt = globalSettingRepository.findByKey(GlobalSetting.KEY_DB_UPDATE_VERSION);
        if (opt.isEmpty()) {
            log.info("execute update0");
            GlobalSetting entity = new GlobalSetting();
            entity.setKey(GlobalSetting.KEY_DB_UPDATE_VERSION);
            entity.setValue("0");
            globalSettingRepository.save(entity);
        }
    }

    private void update1() {
        Optional<GlobalSetting> opt = globalSettingRepository.findByKey(GlobalSetting.KEY_DB_UPDATE_VERSION);
        if (opt.isEmpty()) {
            return;
        }
        GlobalSetting setting = opt.get();
        if (Integer.parseInt(setting.getValue()) < 1) {
            log.info("execute update1");
            GlobalUser admin = new GlobalUser();
            admin.setUserId(UUID.randomUUID());

            String username = "Admin";
            String password = "AXBot";

            admin.setUsername(username);
            admin.setPassword(password);
            globalUserRepository.save(admin);
            log.warn("set Default admin account, username: {}, password: {}. " +
                            "you should always update admin password for security reason",
                    username, password);
            setting.setValue("1");
            globalSettingRepository.save(setting);
        }
    }

}
