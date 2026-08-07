package com.gym.infrastructure.persistence.repository.adapter;

import com.gym.application.port.output.TrainingRepository;
import com.gym.domain.Training;
import com.gym.domain.TrainingType;
import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import com.gym.infrastructure.persistence.entity.TrainingEntity;
import com.gym.infrastructure.persistence.mapper.TrainingMapper;
import com.gym.infrastructure.persistence.repository.jpa.TraineeJpaRepository;
import com.gym.infrastructure.persistence.repository.jpa.TrainerJpaRepository;
import com.gym.infrastructure.persistence.repository.jpa.TrainingJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainingRepositoryAdapter implements TrainingRepository {

    private final TrainingJpaRepository jpa;
    private final TraineeJpaRepository traineeJpa;
    private final TrainerJpaRepository trainerJpa;
    private final TrainingMapper mapper;

    public TrainingRepositoryAdapter(TrainingJpaRepository jpa,
                                     TraineeJpaRepository traineeJpa,
                                     TrainerJpaRepository trainerJpa,
                                     TrainingMapper mapper) {
        this.jpa = jpa;
        this.traineeJpa = traineeJpa;
        this.trainerJpa = trainerJpa;
        this.mapper = mapper;
    }

    @Override
    public Training save(Training training) {
        TraineeEntity trainee = traineeJpa.findByUser_Id(training.getTraineeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Trainee not found: userId=" + training.getTraineeId()));
        TrainerEntity trainer = trainerJpa.findByUser_Id(training.getTrainerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Trainer not found: userId=" + training.getTrainerId()));

        TrainingEntity entity = mapper.toEntity(training, trainee, trainer);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public List<Training> findTraineeTrainings(String username, LocalDate from, LocalDate to,
                                               String trainerName, TrainingType type) {
        return jpa.findTraineeTrainings(username, from, to, trainerName, type)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Training> findTrainerTrainings(String username, LocalDate from, LocalDate to,
                                               String traineeName) {
        return jpa.findTrainerTrainings(username, from, to, traineeName)
                .stream().map(mapper::toDomain).toList();
    }
}