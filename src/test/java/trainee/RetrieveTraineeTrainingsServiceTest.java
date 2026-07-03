package trainee;

import com.gym.application.port.output.TrainingRepository;
import com.gym.application.usecase.trainee.RetrieveTraineeTrainingsService;
import com.gym.domain.Training;
import com.gym.domain.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveTraineeTrainingsServiceTest {

    @Mock TrainingRepository trainingRepository;

    @InjectMocks
    RetrieveTraineeTrainingsService service;

    @Test
    void returns_trainings_with_no_filters() {
        Training training = new Training();
        when(trainingRepository.findTraineeTrainings("Alimardon.Umidov", null, null, null, null))
                .thenReturn(List.of(training));

        List<Training> result = service.getTraineeTrainings("Alimardon.Umidov", null, null, null, null);

        assertThat(result).hasSize(1);
    }

    @Test
    void passes_filters_to_repository() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 12, 31);
        when(trainingRepository.findTraineeTrainings("Alimardon.Umidov", from, to, "Coach", TrainingType.CARDIO))
                .thenReturn(List.of());

        service.getTraineeTrainings("Alimardon.Umidov", from, to, "Coach", TrainingType.CARDIO);

        verify(trainingRepository).findTraineeTrainings("Alimardon.Umidov", from, to, "Coach", TrainingType.CARDIO);
    }

    @Test
    void throws_on_blank_username() {
        assertThatThrownBy(() -> service.getTraineeTrainings("", null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}