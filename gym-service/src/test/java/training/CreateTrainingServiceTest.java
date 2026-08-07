package training;

import com.gym.application.exception.TrainingCreationException;
import com.gym.application.port.output.TrainingRepository;
import com.gym.application.usecase.training.CreateTrainingService;
import com.gym.domain.Training;
import com.gym.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTrainingServiceTest {

    @Mock TrainingRepository trainingRepository;

    @InjectMocks
    CreateTrainingService service;

    Training validTraining;

    @BeforeEach
    void setUp() {
        validTraining = new Training(
                null,
                1L,
                2L,
                "Morning Session",
                TrainingType.CARDIO,
                LocalDate.now().plusDays(1),
                Duration.ofMinutes(60)
        );
    }

    @Test
    void creates_valid_training() {
        when(trainingRepository.save(any(Training.class))).thenAnswer(inv -> inv.getArgument(0));

        Training result = service.addTraining(validTraining);

        assertThat(result.getTrainingName()).isEqualTo("Morning Session");
        verify(trainingRepository).save(validTraining);
    }

    @Test
    void throws_on_past_date() {
        Training past = new Training(
                null, 1L, 2L, "Past", TrainingType.CARDIO,
                LocalDate.now().minusDays(1), Duration.ofMinutes(30)
        );

        assertThatThrownBy(() -> service.addTraining(past))
                .isInstanceOf(TrainingCreationException.class);

        verify(trainingRepository, never()).save(any());
    }

    @Test
    void throws_on_missing_type() {
        Training noType = new Training(
                null, 1L, 2L, "X", null,
                LocalDate.now().plusDays(1), Duration.ofMinutes(30)
        );

        assertThatThrownBy(() -> service.addTraining(noType))
                .isInstanceOf(TrainingCreationException.class);
    }

    @Test
    void throws_on_zero_duration() {
        Training zeroDur = new Training(
                null, 1L, 2L, "X", TrainingType.CARDIO,
                LocalDate.now().plusDays(1), Duration.ZERO
        );

        assertThatThrownBy(() -> service.addTraining(zeroDur))
                .isInstanceOf(TrainingCreationException.class);
    }
}