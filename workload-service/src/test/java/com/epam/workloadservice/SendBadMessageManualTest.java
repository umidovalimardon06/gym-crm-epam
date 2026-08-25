package com.epam.workloadservice;

import com.epam.workload.WorkloadServiceApplication;
import com.epam.workload.domain.ActionType;
import com.epam.workload.dto.WorkloadRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;

@SpringBootTest(classes = WorkloadServiceApplication.class)
class SendBadMessageManualTest {

    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    void sendInvalidWorkloadMessage() {
        WorkloadRequest invalid = new WorkloadRequest(
                "", "Test", "Test", true,
                LocalDate.of(2026, 1, 1), 30, ActionType.ADD);

        jmsTemplate.convertAndSend("workload.queue", invalid);
    }

    @Test
    void sendInvalidWorkloadMessageWithEmptyFirstName() {
        WorkloadRequest invalid = new WorkloadRequest(
                "test-user", "", "Test", true,
                LocalDate.of(2026, 1, 1), 30, ActionType.ADD);

        jmsTemplate.convertAndSend("workload.queue", invalid);
    }

    @Test
    void sendInvalidWorkloadMessageWithZeroDuration() {
        WorkloadRequest invalid = new WorkloadRequest(
                "test-user", "Test", "Test", true,
                LocalDate.of(2026, 1, 1), 0, ActionType.ADD);

        jmsTemplate.convertAndSend("workload.queue", invalid);
    }
}