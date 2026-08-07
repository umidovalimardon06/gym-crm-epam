package com.gym.application.usecase.training;

import com.gym.application.port.input.training.retrieve.GetTrainingTypesUseCase;
import com.gym.domain.TrainingType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTrainingTypesService implements GetTrainingTypesUseCase {
    @Override
    public List<TrainingType> getTrainingTypes() {
        return List.of(TrainingType.values());
    }
}