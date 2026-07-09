package auth;

import com.gym.application.exception.AuthenticationException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.usecase.auth.AuthenticationService;
import com.gym.domain.Trainee;
import com.gym.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock TraineeRepository traineeRepository;
    @Mock TrainerRepository trainerRepository;

    @InjectMocks
    AuthenticationService service;
    Trainee testTrainee;
    Trainer testTrainer;

    @BeforeEach
    void setUp() {
        testTrainee = new Trainee();
        testTrainee.setUsername("Ali.Vip");
        testTrainee.setPassword("123");
        testTrainee.setActive(true);

        testTrainer = new Trainer();
        testTrainer.setUsername("Ilkhom.Vip");
        testTrainer.setPassword("456");
        testTrainer.setActive(true);
    }

    @Test
    void authenticates_valid_trainee() {
        when(traineeRepository.findByUsername("Ali.Vip")).thenReturn(Optional.of(testTrainee));

        assertThatCode(() ->
                service.authenticate(new AuthCredentials("Ali.Vip", "123"))
        ).doesNotThrowAnyException();
    }

    @Test
    void authenticates_valid_trainer() {
        when(trainerRepository.findByUsername("Ilkhom.Vip")).thenReturn(Optional.of(testTrainer));

        assertThatCode(() ->
                service.authenticate(new AuthCredentials("Ilkhom.Vip", "456"))
        ).doesNotThrowAnyException();
    }

    @Test
    void rejects_wrong_password_for_trainee() {
        when(traineeRepository.findByUsername("Ali.Vip")).thenReturn(Optional.of(testTrainee));

        assertThatThrownBy(() ->
                service.authenticate(new AuthCredentials("Ali.Vip", "xxx"))
        ).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void rejects_wrong_password_for_trainer() {
        when(trainerRepository.findByUsername("Ilkhom.Vip")).thenReturn(Optional.of(testTrainer));

        assertThatThrownBy(() ->
                service.authenticate(new AuthCredentials("Ilkhom.Vip", "xxx"))
        ).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void rejects_inactive_user_trainee() {
        testTrainee.setActive(false);
        when(traineeRepository.findByUsername("Ali.Vip")).thenReturn(Optional.of(testTrainee));

        assertThatThrownBy(() ->
                service.authenticate(new AuthCredentials("Ali.Vip", "123"))
        ).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void rejects_inactive_user_trainer() {
        testTrainer.setActive(false);
        when(trainerRepository.findByUsername("Ilkhom.Vip")).thenReturn(Optional.of(testTrainer));

        assertThatThrownBy(() ->
                service.authenticate(new AuthCredentials("Ilkhom.Vip", "123"))
        ).isInstanceOf(AuthenticationException.class);
    }
}