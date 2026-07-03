package trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.usecase.trainee.UpdateTraineeService;
import com.gym.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTraineeServiceTest {

    @Mock AuthenticateUseCase authenticator;
    @Mock TraineeRepository traineeRepository;

    @InjectMocks
    UpdateTraineeService service;

    AuthCredentials auth;
    Trainee existing;

    @BeforeEach
    void setUp() {
        auth = new AuthCredentials("Alimardon.Umidov", "secret");
        existing = new Trainee();
        existing.setUsername("Alimardon.Umidov");
        existing.setFirstName("Alimardon");
        existing.setLastName("Umidov");
        existing.setAddress("Old Address");
        existing.setActive(true);
    }

    @Test
    void updates_trainee_fields() {
        Trainee patch = new Trainee();
        patch.setAddress("New Address");
        patch.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patch.setActive(true);

        when(traineeRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.of(existing));
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        service.updateTraineeProfile(auth, patch);

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeRepository).save(captor.capture());
        Trainee saved = captor.getValue();
        assertThat(saved.getAddress()).isEqualTo("New Address");
        assertThat(saved.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void throws_when_not_found() {
        when(traineeRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateTraineeProfile(auth, new Trainee()))
                .isInstanceOf(NotFoundException.class);
    }
}