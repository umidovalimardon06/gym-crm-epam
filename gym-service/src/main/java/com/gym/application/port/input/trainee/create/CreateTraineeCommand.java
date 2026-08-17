package com.gym.application.port.input.trainee.create;

import java.time.LocalDate;

public record CreateTraineeCommand(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address
) {}