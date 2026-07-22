package com.gym.infrastructure.actuator;

import com.gym.infrastructure.persistence.repository.jpa.TrainingJpaRepository;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "training-stats")
public class TrainingStatsEndpoint {

    private final TrainingJpaRepository trainingRepo;

    public TrainingStatsEndpoint(TrainingJpaRepository trainingRepo) {
        this.trainingRepo = trainingRepo;
    }

    @ReadOperation
    public Map<String, Object> stats() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalTrainings", trainingRepo.count());
        return result;
    }
}