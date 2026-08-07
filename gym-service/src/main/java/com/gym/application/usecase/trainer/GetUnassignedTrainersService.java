package com.gym.application.usecase.trainer;

import com.gym.application.port.input.trainer.retrieve.GetUnassignedTrainersUseCase;
import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetUnassignedTrainersService implements GetUnassignedTrainersUseCase {

    private final TrainerRepository trainerRepository;

    public GetUnassignedTrainersService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        if (traineeUsername == null || traineeUsername.isBlank())
            throw new IllegalArgumentException("traineeUsername is required");
        return trainerRepository.findUnassignedActiveTrainersForTrainee(traineeUsername);
    }
}