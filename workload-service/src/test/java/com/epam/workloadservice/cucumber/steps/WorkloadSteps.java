package com.epam.workloadservice.cucumber.steps;

import com.epam.workload.domain.ActionType;
import com.epam.workload.dto.WorkloadRequest;
import com.epam.workload.repository.TrainerWorkloadRepository;
import com.epam.workload.service.WorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WorkloadSteps {

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private TrainerWorkloadRepository repository;

    @Given("a trainer workload does not exist for username {string}")
    public void trainerWorkloadDoesNotExist(String username) {
        assertFalse(repository.findByUsername(username).isPresent());
    }

    @When("I add a workload for trainer {string} with first name {string}, last name {string}, date {string} and duration {int} minutes")
    public void addWorkload(
            String username,
            String firstName,
            String lastName,
            String date,
            int trainingDuration) {

        WorkloadRequest request = new WorkloadRequest(
                username,
                firstName,
                lastName,
                true,
                LocalDate.parse(date),
                trainingDuration,
                ActionType.ADD
        );

        workloadService.applyWorkload(request);
    }

    @Then("the monthly workload for trainer {string} should be {int} minutes")
    public void monthlyWorkloadShouldBe(
            String username,
            int expectedDuration) {

        Integer duration = workloadService.getMonthlyDuration(
                username,
                2026,
                9
        );

        assertEquals(expectedDuration, duration);
    }

    @Given("a trainer workload exists for username {string} with {int} minutes for {string}")
    public void trainerWorkloadExists(String username, int duration, String date) {
        WorkloadRequest request = new WorkloadRequest(
                username,
                "John",
                "Smith",
                true,
                LocalDate.parse(date),
                duration,
                ActionType.ADD
        );

        workloadService.applyWorkload(request);
    }

    @When("I delete a workload for trainer {string} with date {string} and duration {int} minutes")
    public void deleteWorkload(String username, String date, int duration) {
        WorkloadRequest request = new WorkloadRequest(
                username,
                "John",
                "Smith",
                true,
                LocalDate.parse(date),
                duration,
                ActionType.DELETE
        );

        workloadService.applyWorkload(request);
    }
}