package com.gym.infrastructure.persistence.mapper;

import com.gym.domain.Training;
import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import com.gym.infrastructure.persistence.entity.TrainingEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class TrainingMapper {

    public Training toDomain(TrainingEntity e) {
        if (e == null) return null;
        return new Training(
                e.getId(),
                e.getTrainee().getId(),
                e.getTrainer().getId(),
                e.getTrainingName(),
                e.getTrainingType(),
                e.getTrainingDate(),
                Duration.ofMinutes(e.getTrainingDurationMinutes())
        );
    }

    public TrainingEntity toEntity(Training d, TraineeEntity trainee, TrainerEntity trainer) {
        if (d == null) return null;
        return new TrainingEntity(
                trainee,
                trainer,
                d.getTrainingName(),
                d.getTrainingType(),
                d.getTrainingDate(),
                (int) d.getTrainingDuration().toMinutes()
        );
    }
}