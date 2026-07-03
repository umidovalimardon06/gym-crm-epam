package trainer;

import com.gym.application.port.input.trainer.create.CreateTrainerCommand;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.service.PasswordGenerator;
import com.gym.application.service.UsernameGenerator;
import com.gym.application.usecase.trainer.CreateTrainerService;
import com.gym.domain.Trainer;
import com.gym.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTrainerServiceTest {

    @Mock TrainerRepository trainerRepository;
    @Mock UsernameGenerator usernameGenerator;
    @Mock PasswordGenerator passwordGenerator;

    @InjectMocks
    CreateTrainerService service;

    CreateTrainerCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateTrainerCommand("Coach", "Temur", TrainingType.CARDIO);
    }

    @Test
    void creates_trainer_with_generated_credentials() {
        when(usernameGenerator.generate("Coach", "Temur")).thenReturn("Coach.Temur");
        when(passwordGenerator.generatePassword()).thenReturn("123");
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        Trainer result = service.createTrainer(command);

        assertThat(result.getUsername()).isEqualTo("Coach.Temur");
        assertThat(result.getPassword()).isEqualTo("123");
        assertThat(result.getSpecialization()).isEqualTo(TrainingType.CARDIO);
        assertThat(result.isActive()).isTrue();
    }

    @Test
    void throws_on_null_command() {
        assertThatThrownBy(() -> service.createTrainer(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}