package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.WtNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WtNewsRepository extends JpaRepository<WtNews, Long> {
    boolean existsByUrl(String url);

}