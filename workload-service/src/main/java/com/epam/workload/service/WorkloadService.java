package com.epam.workload.service;

import com.epam.workload.domain.ActionType;
import com.epam.workload.domain.TrainerWorkload;
import com.epam.workload.dto.WorkloadRequest;
import com.epam.workload.repository.TrainerWorkloadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class WorkloadService {
    private static final Logger log = LoggerFactory.getLogger(WorkloadService.class);
    private final TrainerWorkloadRepository repository;

    public WorkloadService(TrainerWorkloadRepository repository) {
        this.repository = repository;
    }

    public void applyWorkload(WorkloadRequest request) {
        log.info("Processing workload: trainer={}, action={}", request.username(), request.actionType());
        log.debug("Searching trainer by username: {}", request.username());

        TrainerWorkload workload = repository
                .findByUsername(request.username())
                .orElseGet(() -> {
                    log.debug("Trainer not found, creating new trainer: {}", request.username());
                    return new TrainerWorkload(
                            request.username(),
                            request.firstName(),
                            request.lastName(),
                            request.isActive()
                    );
                });

        log.debug("Updating trainer profile: username={}, active={}", request.username(), request.isActive());
        workload.setActive(request.isActive());
        workload.setFirstName(request.firstName());
        workload.setLastName(request.lastName());

        LocalDate date = request.trainingDate();
        int year = date.getYear();
        int month = date.getMonthValue();

        log.debug("Updating training summary: trainer={}, year={}, month={}, duration={}, action={}",
                request.username(), year, month, request.trainingDuration(), request.actionType());

        if (request.actionType() == ActionType.ADD) {
            workload.addDuration(year, month, request.trainingDuration());
        } else {
            workload.subtractDuration(year, month, request.trainingDuration());
        }

        log.debug("Saving trainer workload: {}", request.username());
        repository.save(workload);
        log.info("Workload processed successfully: trainer={}", request.username());
    }

    public Integer getMonthlyDuration(String username, int year, int month) {
        log.debug("Searching monthly duration: trainer={}, year={}, month={}", username, year, month);
        Integer duration = repository.findByUsername(username)
                .map(workload -> workload.getDuration(year, month))
                .orElse(null);
        log.debug("Monthly duration retrieved: trainer={}, year={}, month={}, duration={}", username, year, month, duration);
        return duration;
    }

    public TrainerWorkload getWorkload(String username) {
        log.debug("Searching trainer workload: {}", username);
        TrainerWorkload workload = repository.findByUsername(username).orElse(null);
        log.debug("Trainer workload {}: {}", workload != null ? "found" : "not found", username);
        return workload;
    }
}