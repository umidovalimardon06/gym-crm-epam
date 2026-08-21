package com.epam.workload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class WorkloadDeadLetterListener {

    private static final Logger log = LoggerFactory.getLogger(WorkloadDeadLetterListener.class);

    @JmsListener(destination = "ActiveMQ.DLQ", containerFactory = "dlqJmsListenerContainerFactory")
    public void onDeadLetter(String rawMessageBody) {
        log.error("Dead-lettered workload message received after exhausting retries. " +
                        "This message could not be processed and requires manual investigation. Body: {}",
                rawMessageBody);
    }
}