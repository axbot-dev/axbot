package com.github.axiangcoding.app.server.data.repo;

import com.github.axiangcoding.app.server.data.entity.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {
}