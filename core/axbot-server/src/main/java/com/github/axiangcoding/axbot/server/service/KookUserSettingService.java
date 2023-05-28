package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.SponsorOrder;
import com.github.axiangcoding.axbot.server.data.entity.basic.BindProfile;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserPermit;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserSubscribe;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserUsage;
import com.github.axiangcoding.axbot.server.data.repository.KookUserSettingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KookUserSettingService {
    @Resource
    KookUserSettingRepository kookUserSettingRepository;

    public KookUserSetting newSetting(String userId) {
        KookUserSetting entity = KookUserSetting.defaultSetting(userId);
        return kookUserSettingRepository.save(entity);
    }

    public KookUserSetting getOrDefault(String userId) {
        KookUserSetting userSetting;
        Optional<KookUserSetting> opt = findByUserId(userId);
        userSetting = opt.orElseGet(() -> newSetting(userId));
        return userSetting;
    }

    public Optional<KookUserSetting> findByUserId(String userId) {
        return kookUserSettingRepository.findByUserId(userId);
    }

    public List<KookUserSetting> findAllByBanned(Boolean banned) {
        return kookUserSettingRepository.findAllByBanned(banned);
    }


    public void blockUser(String userId, String reason) {
        log.info("kook user [{}] get blocked, because [{}]", userId, reason);
        kookUserSettingRepository.updateBannedAndBannedReasonAndBannedTimeByUserId(true, reason, LocalDateTime.now(), userId);
    }

    public void unblockUser(String userId) {
        log.info("kook user [{}] get unblocked", userId);
        kookUserSettingRepository.updateBannedAndBannedReasonAndBannedTimeByUserId(false, null, null, userId);
    }

    public void updateInputUsage(KookUserSetting setting) {
        UserUsage usage = setting.getUsage();
        usage.setInputToday(usage.getInputToday() + 1);
        usage.setInputTotal(usage.getInputTotal() + 1);
        kookUserSettingRepository.save(setting);
    }

    public void resetTodayUsage() {
        kookUserSettingRepository.resetTodayUsage();
    }

    public boolean canUseAI(String userId) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return false;
        }
        KookUserSetting entity = opt.get();
        UserPermit permit = entity.getPermit();
        if (permit == null) {
            return false;
        }
        return Boolean.TRUE.equals(permit.getCanUseAIChat());
    }

    public void setCanUseAI(String userId, boolean canUseAI) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        KookUserSetting entity = opt.get();
        if (entity.getPermit() == null) {
            entity.setPermit(UserPermit.defaultPermit());
        }
        UserPermit permit = entity.getPermit();
        permit.setCanUseAIChat(canUseAI);
        kookUserSettingRepository.save(entity);
    }

    public void updateSubscribe(String userId, String plan, int month) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        KookUserSetting entity = opt.get();
        if (entity.getSubscribe() == null) {
            entity.setSubscribe(new UserSubscribe());
        }
        UserSubscribe subscribe = entity.getSubscribe();
        subscribe.setPlan(plan);
        LocalDateTime startTime;
        LocalDateTime now = LocalDateTime.now();
        // 如果过期时间为空或者已经过期了，那么从现在开始计算
        if (subscribe.getExpireAt() == null || subscribe.getExpireAt().isBefore(now)) {
            startTime = now;
        }
        // 如果还没过期，那么从过期时间开始计算
        else {
            startTime = subscribe.getExpireAt();
        }
        LocalDateTime endTime = startTime.plusMonths(month);
        subscribe.setExpireAt(endTime);
        kookUserSettingRepository.save(entity);
    }

    public int getInputLimit(String userId) {
        if (isSubscribedBasicPlan(userId)) {
            return 10000;
        } else {
            return 100;
        }
    }

    public boolean isSubscribedBasicPlan(String userId) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return false;
        }
        KookUserSetting setting = opt.get();
        UserSubscribe subscribe = setting.getSubscribe();
        if (subscribe != null) {
            boolean b = SponsorOrder.PLAN.BASIC_PERSONAL.getName().equals(subscribe.getPlan());
            return b && subscribe.getExpireAt().isAfter(LocalDateTime.now());
        } else {
            return false;
        }
    }

    public void bindWtNickname(String userId, String nickname) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        KookUserSetting setting = opt.get();
        BindProfile bindProfile = setting.getBindProfile();
        if (bindProfile == null) {
            bindProfile = new BindProfile();
            bindProfile.setWtNickname(nickname);
            setting.setBindProfile(bindProfile);
        } else {
            bindProfile.setWtNickname(nickname);
        }
        kookUserSettingRepository.save(setting);
    }

    public void unbindWtNickname(String userId) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        KookUserSetting setting = opt.get();
        BindProfile bindProfile = setting.getBindProfile();
        if (bindProfile == null) {
            return;
        } else {
            bindProfile.setWtNickname(null);
        }
        kookUserSettingRepository.save(setting);
    }

    public List<KookUserSetting> findByBindWtNickname(String nickname) {
        return kookUserSettingRepository.findByBindProfileWtNickname(nickname);
    }

    public List<KookUserSetting> getKookSuperAdminUser() {
        return kookUserSettingRepository.findByRole(KookUserSetting.ROLE.SUPER_ADMIN.getLabel());
    }

    public void setKookSuperAdminUser(String userId) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        KookUserSetting setting = opt.get();
        setting.setRole(KookUserSetting.ROLE.SUPER_ADMIN.getLabel());
        kookUserSettingRepository.save(setting);
    }
}
