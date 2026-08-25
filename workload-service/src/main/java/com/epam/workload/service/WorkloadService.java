package com.epam.workload.service;

import com.epam.workload.domain.ActionType;
import com.epam.workload.domain.TrainerWorkload;
import com.epam.workload.dto.WorkloadRequest;
import com.epam.workload.repository.TrainerWorkloadRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class WorkloadService {

    private final TrainerWorkloadRepository repository;

    public WorkloadService(TrainerWorkloadRepository repository) {
        this.repository = repository;
    }

    public void applyWorkload(WorkloadRequest request) {

        TrainerWorkload workload = repository
                .findByUsername(request.username())
                .orElseGet(() -> new TrainerWorkload(
                        request.username(),
                        request.firstName(),
                        request.lastName(),
                        request.isActive()
                ));

        workload.setActive(request.isActive());
        workload.setFirstName(request.firstName());
        workload.setLastName(request.lastName());

        LocalDate date = request.trainingDate();
        int year = date.getYear();
        int month = date.getMonthValue();

        if (request.actionType() == ActionType.ADD) {
            workload.addDuration(
                    year,
                    month,
                    request.trainingDuration()
            );
        } else {
            workload.subtractDuration(
                    year,
                    month,
                    request.trainingDuration()
            );
        }

        repository.save(workload);
    }

    public Integer getMonthlyDuration(String username, int year, int month) {
        return repository.findByUsername(username)
                .map(workload -> workload.getDuration(year, month))
                .orElse(null);
    }

    public TrainerWorkload getWorkload(String username) {
        return repository.findByUsername(username)
                .orElse(null);
    }
}