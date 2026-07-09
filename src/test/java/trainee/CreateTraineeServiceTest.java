package trainee;

import com.gym.application.exception.CreationCommandException;
import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.service.PasswordGenerator;
import com.gym.application.service.UsernameGenerator;
import com.gym.application.usecase.trainee.CreateTraineeService;
import com.gym.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTraineeServiceTest {
    @Mock TraineeRepository traineeRepository;
    @Mock UsernameGenerator usernameGenerator;
    @Mock PasswordGenerator passwordGenerator;
    @InjectMocks
    CreateTraineeService service;
    CreateTraineeCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateTraineeCommand(
                "Alimardon", "Umidov", LocalDate.of(2006, 1, 1), "Uzbekistan Khorezm"
        );
    }

    @Test
    void creates_trainee_with_generated_credentials() {
        when(usernameGenerator.generate("Alimardon", "Umidov")).thenReturn("Alimardon.Umidov");
        when(passwordGenerator.generatePassword()).thenReturn("123");
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        Trainee result = service.create(command);

        assertThat(result.getUsername()).isEqualTo("Alimardon.Umidov");
        assertThat(result.getPassword()).isEqualTo("123");
        assertThat(result.getFirstName()).isEqualTo("Alimardon");
        assertThat(result.getLastName()).isEqualTo("Umidov");
        assertThat(result.isActive()).isTrue();
    }

    @Test
    void throws_on_null_command() {
        assertThatThrownBy(() -> service.create(null))
                .isInstanceOf(CreationCommandException.class);
    }
}