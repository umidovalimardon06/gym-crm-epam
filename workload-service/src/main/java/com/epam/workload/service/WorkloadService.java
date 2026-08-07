package com.epam.workload.service;
import com.epam.workload.domain.ActionType;
import com.epam.workload.domain.TrainerWorkload;
import com.epam.workload.dto.WorkloadRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkloadService {

    private final Map<String, TrainerWorkload> store = new ConcurrentHashMap<>();

    public void applyWorkload(WorkloadRequest request) {
        TrainerWorkload workload = store.computeIfAbsent(request.username(),
                u -> new TrainerWorkload(request.username(), request.firstName(),
                        request.lastName(), request.isActive()));

        workload.setActive(request.isActive());
        workload.setFirstName(request.firstName());
        workload.setLastName(request.lastName());

        LocalDate date = request.trainingDate();
        int year = date.getYear();
        int month = date.getMonthValue();

        if (request.actionType() == ActionType.ADD) {
            workload.addDuration(year, month, request.trainingDuration());
        } else {
            workload.subtractDuration(year, month, request.trainingDuration());
        }
    }

    public Integer getMonthlyDuration(String username, int year, int month) {
        TrainerWorkload workload = store.get(username);
        return workload != null ? workload.getDuration(year, month) : null;
    }

    public TrainerWorkload getWorkload(String username) {
        return store.get(username);
    }
}