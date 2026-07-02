package com.gym.application.port.output;

import com.gym.domain.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    Trainee save(Trainee trainee);
    Optional<Trainee> findById(Long id);
    Optional<Trainee> findByUsername(String username);
    List<Trainee> findAll();
    void deleteByUsername(String username);
    boolean existsByUsername(String username);
    Trainee updateTrainers(String traineeUsername, List<Long> trainerUserIds);
}