package com.gym.application.port.output;

import com.gym.domain.User;

import java.util.Optional;

public interface UserRepository {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    User save(User user);
}
