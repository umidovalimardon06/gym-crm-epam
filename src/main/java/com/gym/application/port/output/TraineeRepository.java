package com.gym.application.port.output;

import com.gym.domain.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    Trainee save(Trainee trainee);
    Optional<Trainee> findByUsername(String username);
    Trainee updateTrainers(String traineeUsername, List<Long> trainerUserIds);
    void deleteByUsername(String username);
    /*List<Trainee> findAll();
    Optional<Trainee> findById(Long id);
    boolean existsByUsername(String username);*/
}