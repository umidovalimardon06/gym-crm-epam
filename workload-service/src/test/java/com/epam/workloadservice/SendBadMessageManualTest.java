package com.epam.workloadservice;

import com.epam.workload.domain.ActionType;
import com.epam.workload.dto.WorkloadRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;

@SpringBootTest
class SendBadMessageManualTest {

    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    @Disabled("Run manually to test DLQ behavior — requires ActiveMQ running on localhost:61616")
    void sendInvalidWorkloadMessage() {
        WorkloadRequest invalid = new WorkloadRequest(
                "", "Test", "Test", true,
                LocalDate.of(2026, 1, 1), 30, ActionType.ADD);

        jmsTemplate.convertAndSend("workload.queue", invalid);
    }
}