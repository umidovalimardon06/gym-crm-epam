package com.gym.infrastructure.persistence.mapper;

import com.gym.domain.Trainer;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    private final UserMapper userMapper;

    public TrainerMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Trainer toDomain(TrainerEntity e) {
        if (e == null) return null;
        Trainer t = new Trainer();
        userMapper.copyToDomain(e.getUser(), t);
        t.setSpecialization(e.getSpecialization());
        return t;
    }

    public TrainerEntity toEntity(Trainer d) {
        if (d == null) return null;
        return new TrainerEntity(
                d.getSpecialization(),
                userMapper.toEntity(d)
        );
    }
}