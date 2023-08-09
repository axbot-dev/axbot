package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTaskId(String taskId);
}