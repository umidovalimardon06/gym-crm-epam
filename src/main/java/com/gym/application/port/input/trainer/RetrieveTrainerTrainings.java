package com.gym.application.port.input.trainer;

import com.gym.domain.Training;

import java.util.List;

public interface RetrieveTrainerTrainings {
    List<Training> getTrainings(String username);
}
