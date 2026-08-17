package com.gym.infrastructure.persistence.repository.adapter;

import com.gym.application.port.output.UserRepository;
import com.gym.domain.User;
import com.gym.infrastructure.persistence.entity.UserEntity;
import com.gym.infrastructure.persistence.mapper.UserMapper;
import com.gym.infrastructure.persistence.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpa;
    private final UserMapper mapper;

    public UserRepositoryAdapter(UserJpaRepository jpa, UserMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByUsername(username).map(this::toUser);
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getUserId() == null) {
            throw new IllegalStateException("Cannot save user without id");
        }
        UserEntity existing = jpa.findById(user.getUserId())
                .orElseThrow(() -> new IllegalStateException(
                        "User not found: id=" + user.getUserId()));

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setUsername(user.getUsername());
        existing.setPassword(user.getPassword());
        existing.setActive(user.isActive());

        return toUser(jpa.save(existing));
    }

    private User toUser(UserEntity e) {
        User u = new User();
        mapper.copyToDomain(e, u);
        return u;
    }
}