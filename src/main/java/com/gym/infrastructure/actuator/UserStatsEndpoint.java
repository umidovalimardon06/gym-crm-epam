package com.gym.infrastructure.actuator;

import com.gym.infrastructure.persistence.repository.jpa.TraineeJpaRepository;
import com.gym.infrastructure.persistence.repository.jpa.TrainerJpaRepository;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Endpoint(id = "user-stats")
public class UserStatsEndpoint {
    private final TraineeJpaRepository traineeRepository;
    private final TrainerJpaRepository trainerRepository;

    public UserStatsEndpoint(TraineeJpaRepository traineeRepository, TrainerJpaRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @ReadOperation
    public Map<String,String> stats() {
        return Map.of(
            "traineeCount", String.valueOf(traineeRepository.count()),
            "trainerCount", String.valueOf(trainerRepository.count())
        );
    }
}