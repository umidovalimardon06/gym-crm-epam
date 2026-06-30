package com.gym.infrastructure.persistence.repository.adapter;

import com.gym.application.port.output.UsernameRepository;
import com.gym.infrastructure.persistence.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UsernameRepositoryAdapter implements UsernameRepository {
    private final UserJpaRepository userJpaRepository;

    public UsernameRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
}