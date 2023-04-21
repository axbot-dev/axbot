package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.TextCensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TextCensorRepository extends JpaRepository<TextCensor, Long> {
    Optional<TextCensor> findByText(String text);
}