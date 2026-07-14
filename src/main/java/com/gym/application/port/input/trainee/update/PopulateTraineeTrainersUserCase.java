package com.gym.application.port.input.trainee.update;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.domain.Trainee;

import java.util.Set;

public interface PopulateTraineeTrainersUserCase {
    Trainee populateTraineeTrainers(AuthCredentials authCredentials, String traineeUsername, Set<Long> trainerIds);
}
