package trainee;

import com.gym.application.exception.TraineeDeletionException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.usecase.trainee.DeleteTraineeService;
import com.gym.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteTraineeServiceTest {
    @Mock AuthenticateUseCase authenticator;
    @Mock TraineeRepository traineeRepository;
    @InjectMocks
    DeleteTraineeService service;

    AuthCredentials auth;
    Trainee trainee;

    @BeforeEach
    void setUp() {
        auth = new AuthCredentials("Alimardon.Umidov", "secret");
        trainee = new Trainee();
        trainee.setUsername("Alimardon.Umidov");
    }

    @Test
    void deletes_own_account() {
        when(traineeRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.of(trainee));

        Trainee result = service.deleteTrainee(auth, "Alimardon.Umidov");

        assertThat(result.getUsername()).isEqualTo("Alimardon.Umidov");
        verify(traineeRepository).deleteByUsername("Alimardon.Umidov");
    }

    @Test
    void throws_when_deleting_another_user() {
        assertThatThrownBy(() -> service.deleteTrainee(auth, "Other.User"))
                .isInstanceOf(TraineeDeletionException.class);
    }
}