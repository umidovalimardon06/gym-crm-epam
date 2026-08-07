package com.epam.workload.controller;

import com.epam.workload.dto.WorkloadRequest;
import com.epam.workload.dto.WorkloadSummaryResponse;
import com.epam.workload.service.WorkloadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workload")
public class WorkloadController {

    private final WorkloadService workloadService;

    public WorkloadController(WorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @PostMapping
    public ResponseEntity<Void> handleWorkload(@Valid @RequestBody WorkloadRequest request) {
        workloadService.applyWorkload(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/{year}/{month}")
    public ResponseEntity<WorkloadSummaryResponse> getMonthlySummary(
            @PathVariable String username,
            @PathVariable int year,
            @PathVariable int month) {

        Integer duration = workloadService.getMonthlyDuration(username, year, month);
        if (duration == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new WorkloadSummaryResponse(username, year, month, duration));
    }
}