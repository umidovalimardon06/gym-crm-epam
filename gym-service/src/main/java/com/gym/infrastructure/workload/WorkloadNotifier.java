package com.gym.infrastructure.workload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkloadNotifier {
    private static final Logger log = LoggerFactory.getLogger(WorkloadNotifier.class);
    public static final String QUEUE_NAME = "workload.queue";

    private final JmsTemplate jmsTemplate;

    public WorkloadNotifier(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void notify(WorkloadRequest request) {
        try {
            jmsTemplate.convertAndSend(QUEUE_NAME, request);
            log.info("Workload message sent: trainer={}, action={}", request.username(), request.actionType());
        } catch (Exception e) {
            log.warn("Failed to send workload message for trainer={}, action={}: {}",
                    request.username(), request.actionType(), e.getMessage());
        }
    }
}