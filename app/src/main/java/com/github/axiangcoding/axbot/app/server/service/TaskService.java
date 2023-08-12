package com.github.axiangcoding.axbot.app.server.service;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.server.data.entity.Task;
import com.github.axiangcoding.axbot.app.server.data.repo.TaskRepository;
import com.github.axiangcoding.axbot.app.server.service.entity.SyncPlayerTaskConfig;
import com.github.axiangcoding.axbot.app.server.service.entity.SyncPlayerTaskResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TaskService {
    @Resource
    private TaskRepository repository;

    public String startNewTask(SyncPlayerTaskConfig syncPlayerTaskConfig) {
        return repository.save(Task.initSyncPlayer(
                JSONObject.toJSONString(syncPlayerTaskConfig))).getTaskId();
    }

    public Optional<Task> findByTaskId(String taskId) {
        return repository.findByTaskId(taskId);
    }

    public void updateProgress(String taskId, double progress) {
        if (progress < 0 || progress > 1) {
            throw new IllegalArgumentException("progress must be between 0 and 1");
        }
        repository.findByTaskId(taskId).ifPresent(task -> {
            task.setProgress(progress);
            repository.save(task);
        });
    }

    public void finishedWithResult(String taskId, SyncPlayerTaskResult result) {
        repository.findByTaskId(taskId).ifPresent(task -> {
            task.setProgress(1.0);
            task.setResult(JSONObject.toJSONString(result));
            task.setStatus(Task.STATUS.FINISHED.name());
            repository.save(task);
        });
    }

    public void failedWithResult(String taskId, String reason) {
        repository.findByTaskId(taskId).ifPresent(task -> {
            task.setResult(reason);
            task.setStatus(Task.STATUS.FAILED.name());
            repository.save(task);
        });
    }
}
