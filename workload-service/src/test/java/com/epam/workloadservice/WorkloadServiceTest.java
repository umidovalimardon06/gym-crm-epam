package com.epam.workloadservice;

import com.epam.workload.domain.ActionType;
import com.epam.workload.domain.TrainerWorkload;
import com.epam.workload.dto.WorkloadRequest;
import com.epam.workload.repository.TrainerWorkloadRepository;
import com.epam.workload.service.WorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkloadServiceTest {
    @Mock
    private TrainerWorkloadRepository repository;
    private WorkloadService workloadService;

    @BeforeEach
    void setUp() {workloadService = new WorkloadService(repository);}

    @Test
    void shouldCreateTrainerAndAddWorkload() {
        WorkloadRequest request = new WorkloadRequest(
                "akhmedov",
                "Firdavs",
                "Akhmedov",
                true,
                LocalDate.of(2026, 8, 15),
                180,
                ActionType.ADD
        );

        when(repository.findByUsername("akhmedov")).thenReturn(Optional.empty());
        workloadService.applyWorkload(request);
        TrainerWorkload saved = getSavedWorkload();

        assertEquals("akhmedov", saved.getUsername());
        assertEquals("Firdavs", saved.getFirstName());
        assertEquals("Akhmedov", saved.getLastName());
        assertTrue(saved.isActive());
        assertEquals(180, saved.getDuration(2026, 8));
    }

    @Test
    void shouldAddDurationToExistingTrainer() {
        TrainerWorkload existing = new TrainerWorkload(
                "akhmedov",
                "Firdavs",
                "Akhmedov",
                true
        );

        existing.addDuration(2026, 8, 180);

        WorkloadRequest request = new WorkloadRequest(
                "akhmedov",
                "Firdavs",
                "Akhmedov",
                true,
                LocalDate.of(2026, 8, 20),
                60,
                ActionType.ADD
        );

        when(repository.findByUsername("akhmedov")).thenReturn(Optional.of(existing));
        workloadService.applyWorkload(request);
        TrainerWorkload saved = getSavedWorkload();
        assertEquals(240, saved.getDuration(2026, 8));
    }

    @Test
    void shouldSubtractDurationFromExistingTrainer() {
        TrainerWorkload existing = new TrainerWorkload(
                "akhmedov",
                "Firdavs",
                "Akhmedov",
                true
        );

        existing.addDuration(2026, 8, 180);

        WorkloadRequest request = new WorkloadRequest(
                "akhmedov",
                "Firdavs",
                "Akhmedov",
                true,
                LocalDate.of(2026, 8, 20),
                60,
                ActionType.DELETE
        );

        when(repository.findByUsername("akhmedov")).thenReturn(Optional.of(existing));
        workloadService.applyWorkload(request);
        TrainerWorkload saved = getSavedWorkload();
        assertEquals(120, saved.getDuration(2026, 8));
    }

    @Test
    void shouldUpdateTrainerProfile() {
        TrainerWorkload existing = new TrainerWorkload(
                "akhmedov",
                "Firdavs",
                "Akhmedov",
                true
        );

        WorkloadRequest request = new WorkloadRequest(
                "akhmedov",
                "Firdavs",
                "Karimov",
                false,
                LocalDate.of(2026, 8, 20),
                60,
                ActionType.ADD
        );

        when(repository.findByUsername("akhmedov")).thenReturn(Optional.of(existing));
        workloadService.applyWorkload(request);
        TrainerWorkload saved = getSavedWorkload();

        assertEquals("Firdavs", saved.getFirstName());
        assertEquals("Karimov", saved.getLastName());
        assertFalse(saved.isActive());
    }

    private TrainerWorkload getSavedWorkload() {
        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
        return captor.getValue();
    }
}
