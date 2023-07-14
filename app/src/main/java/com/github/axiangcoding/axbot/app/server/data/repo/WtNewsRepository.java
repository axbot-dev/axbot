package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.WtNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WtNewsRepository extends JpaRepository<WtNews, Long> {
    boolean existsByUrl(String url);
}