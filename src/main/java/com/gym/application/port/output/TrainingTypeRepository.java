package com.gym.application.port.output;

import com.gym.domain.TrainingType;
import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository {
    Optional<TrainingType> findById(Long id);
    Optional<TrainingType> findByName(String name);
    List<TrainingType> findAll();
}