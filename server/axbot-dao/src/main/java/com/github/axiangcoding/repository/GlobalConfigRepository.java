package com.github.axiangcoding.repository;

import com.github.axiangcoding.entity.GlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalConfigRepository extends JpaRepository<GlobalConfig, Long> {
}