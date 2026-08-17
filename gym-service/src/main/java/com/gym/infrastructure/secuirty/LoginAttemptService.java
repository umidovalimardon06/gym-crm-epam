package com.gym.infrastructure.secuirty;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_DURATION_MS = 5 * 60 * 1000;
    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lockedUntil = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        int count = attempts.merge(username, 1, Integer::sum);
        if (count >= MAX_ATTEMPTS) {
            lockedUntil.put(username, System.currentTimeMillis() + LOCK_DURATION_MS);
        }
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
        lockedUntil.remove(username);
    }

    public boolean isBlocked(String username) {
        Long until = lockedUntil.get(username);
        if (until == null) return false;
        if (System.currentTimeMillis() > until) {
            loginSucceeded(username);
            return false;
        }
        return true;
    }
}