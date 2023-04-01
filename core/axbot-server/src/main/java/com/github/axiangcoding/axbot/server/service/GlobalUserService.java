package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
import com.github.axiangcoding.axbot.server.data.repository.GlobalUserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GlobalUserService {
    @Resource
    GlobalUserRepository globalUserRepository;


    public boolean checkPwd(String username, String password) {
        // TBD
        return false;
    }

    public Optional<GlobalUser> findByUsername(String username) {
        return globalUserRepository.findByUsername(username);
    }

}
