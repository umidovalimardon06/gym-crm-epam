package com.gym.application.usecase.trainee;

import com.gym.application.port.input.trainee.update.UpdateTraineeTrainersUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateTraineeTrainersService implements UpdateTraineeTrainersUseCase {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public UpdateTraineeTrainersService(TraineeRepository traineeRepository,
                                        TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional
    public List<Trainer> updateAssignedTrainers(String traineeUsername, List<Long> trainerIds) {
        if (traineeUsername == null || traineeUsername.isBlank())
            throw new IllegalArgumentException("traineeUsername is required");
        if (trainerIds == null)
            throw new IllegalArgumentException("trainerIds is required (empty list allowed)");

        var updated = traineeRepository.updateTrainers(traineeUsername, trainerIds);

        // Fetch domain trainers to return
        return updated.getTrainerIds().stream()
                .map(trainerRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
    }
}