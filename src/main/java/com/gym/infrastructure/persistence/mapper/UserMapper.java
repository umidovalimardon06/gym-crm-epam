package com.gym.infrastructure.persistence.mapper;

import com.gym.domain.User;
import com.gym.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public void copyToDomain(UserEntity e, User target) {
        if (e == null || target == null) return;
        target.setUserId(e.getId());
        target.setFirstName(e.getFirstName());
        target.setLastName(e.getLastName());
        target.setUsername(e.getUsername());
        target.setPassword(e.getPassword());
        target.setActive(e.isActive());
    }

    public UserEntity toEntity(User d) {
        if (d == null) return null;
        return new UserEntity(
                d.getFirstName(),
                d.getLastName(),
                d.getUsername(),
                d.getPassword(),
                d.isActive()
        );
    }
}