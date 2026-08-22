package com.epam.workloadservice;

import com.epam.workload.controller.WorkloadMessageListener;
import com.epam.workload.domain.ActionType;
import com.epam.workload.dto.WorkloadRequest;
import com.epam.workload.service.WorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class WorkloadMessageListenerTest {

    @Mock
    private WorkloadService workloadService;

    @InjectMocks
    private WorkloadMessageListener listener;

    @Test
    void shouldProcessValidWorkloadMessage() {
        WorkloadRequest request = new WorkloadRequest(
                "test-user",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 1, 1),
                30,
                ActionType.ADD
        );

        listener.onWorkloadMessage(request);

        verify(workloadService).applyWorkload(request);
    }

    @Test
    void shouldRejectMessageWithEmptyUsername() {
        WorkloadRequest request = new WorkloadRequest(
                "",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 1, 1),
                30,
                ActionType.ADD
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> listener.onWorkloadMessage(request)
        );

        verifyNoInteractions(workloadService);
    }
}