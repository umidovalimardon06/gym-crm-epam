package com.gym.infrastructure.secuirty;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Component
public class TokenBlacklistService {
    private final Set<String> blacklisted = ConcurrentHashMap.newKeySet();

    public void blacklist(String token) {
        blacklisted.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklisted.contains(token);
    }
}