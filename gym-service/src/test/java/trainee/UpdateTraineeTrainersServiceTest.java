package trainee;

import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.usecase.trainee.UpdateTraineeTrainersService;
import com.gym.domain.Trainee;
import com.gym.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTraineeTrainersServiceTest {
    @Mock TraineeRepository traineeRepository;
    @Mock TrainerRepository trainerRepository;
    @InjectMocks
    UpdateTraineeTrainersService service;

    Trainee updatedTrainee;
    Trainer trainer1;
    Trainer trainer2;

    @BeforeEach
    void setUp() {
        trainer1 = new Trainer();
        trainer1.setUserId(10L);
        trainer1.setUsername("Coach.One");

        trainer2 = new Trainer();
        trainer2.setUserId(20L);
        trainer2.setUsername("Coach.Two");

        updatedTrainee = new Trainee();
        updatedTrainee.setUsername("Alimardon.Umidov");
        updatedTrainee.setTrainerIds(new HashSet<>(Set.of(10L, 20L)));
    }

    @Test
    void replaces_trainer_list() {
        when(traineeRepository.updateTrainers("Alimardon.Umidov", List.of(10L, 20L)))
                .thenReturn(updatedTrainee);
        when(trainerRepository.findById(10L)).thenReturn(Optional.of(trainer1));
        when(trainerRepository.findById(20L)).thenReturn(Optional.of(trainer2));

        List<Trainer> result = service.updateAssignedTrainers("Alimardon.Umidov", List.of(10L, 20L));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Trainer::getUsername)
                .containsExactlyInAnyOrder("Coach.One", "Coach.Two");
    }

    @Test
    void throws_on_blank_username() {
        assertThatThrownBy(() -> service.updateAssignedTrainers("", List.of(10L)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}