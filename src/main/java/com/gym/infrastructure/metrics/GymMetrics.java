package com.gym.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GymMetrics {

    private final Counter traineeRegistrations;
    private final Counter trainerRegistrations;
    private final Counter trainingsCreated;
    private final Counter authFailures;

    public GymMetrics(MeterRegistry registry) {
        this.traineeRegistrations = Counter.builder("gym_trainee_registrations_total")
                .description("Total number of trainee registrations")
                .register(registry);

        this.trainerRegistrations = Counter.builder("gym_trainer_registrations_total")
                .description("Total number of trainer registrations")
                .register(registry);

        this.trainingsCreated = Counter.builder("gym_trainings_created_total")
                .description("Total number of trainings created")
                .register(registry);

        this.authFailures = Counter.builder("gym_auth_failures_total")
                .description("Total number of failed authentication attempts")
                .register(registry);

    }

    public void incrementTraineeRegistrations() { traineeRegistrations.increment(); }
    public void incrementTrainerRegistrations() { trainerRegistrations.increment(); }
    public void incrementTrainingsCreated() { trainingsCreated.increment(); }
    public void incrementAuthFailures() { authFailures.increment(); }
}