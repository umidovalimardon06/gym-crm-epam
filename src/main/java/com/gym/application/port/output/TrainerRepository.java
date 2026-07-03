package com.gym.application.port.output;

import com.gym.domain.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    Trainer save(Trainer trainer);
    Optional<Trainer> findById(Long id);
    Optional<Trainer> findByUsername(String username);
   /* List<Trainer> findAll();
    List<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername);
    boolean existsByUsername(String username);*/
    List<Trainer> findUnassignedActiveTrainersForTrainee(String traineeUsername);

    List<Trainer> findAll();

    List<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername);

    boolean existsByUsername(String username);
}