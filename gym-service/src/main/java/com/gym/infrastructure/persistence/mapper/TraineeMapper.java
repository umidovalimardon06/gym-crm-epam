package com.gym.infrastructure.persistence.mapper;

import com.gym.domain.Trainee;
import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class TraineeMapper {

    private final UserMapper userMapper;

    public TraineeMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Trainee toDomain(TraineeEntity e) {
        if (e == null) return null;
        Trainee t = new Trainee();
        userMapper.copyToDomain(e.getUser(), t);
        t.setDateOfBirth(e.getDateOfBirth());
        t.setAddress(e.getAddress());
        t.setTrainerIds(
                e.getTrainers() == null ? new HashSet<>() :
                        e.getTrainers().stream()
                                .map(tr -> tr.getUser().getId())
                                .collect(Collectors.toSet())
        );
        return t;
    }

    public TraineeEntity toEntity(Trainee d) {
        if (d == null) return null;
        return new TraineeEntity(
                d.getDateOfBirth(),
                d.getAddress(),
                userMapper.toEntity(d)
        );
    }
}