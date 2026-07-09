package trainee;

import com.gym.application.exception.InvalidStateException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.usecase.trainee.ChangeTraineeStatusService;
import com.gym.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeTraineeStatusServiceTest {
    @Mock AuthenticateUseCase authenticator;
    @Mock TraineeRepository traineeRepository;
    @InjectMocks
    ChangeTraineeStatusService service;

    AuthCredentials auth;
    Trainee trainee;

    @BeforeEach
    void setUp() {
        auth = new AuthCredentials("Alimardon.Umidov", "123");
        trainee = new Trainee();
        trainee.setUsername("Alimardon.Umidov");
        trainee.setActive(true);
    }

    @Test
    void deactivates_active_trainee() {
        when(traineeRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        service.deactivate(auth);

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeRepository).save(captor.capture());
        assertThat(captor.getValue().isActive()).isFalse();
    }

    @Test
    void activates_inactive_trainee() {
        trainee.setActive(false);
        when(traineeRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        service.activate(auth);

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeRepository).save(captor.capture());
        assertThat(captor.getValue().isActive()).isTrue();
    }

    @Test
    void throws_when_already_in_target_state() {
        when(traineeRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.of(trainee));

        assertThatThrownBy(() -> service.activate(auth))
                .isInstanceOf(InvalidStateException.class);
    }
}