package com.gym.application.port.output;

public interface UsernameRepository {
    boolean existsByUsername(String username);
}
