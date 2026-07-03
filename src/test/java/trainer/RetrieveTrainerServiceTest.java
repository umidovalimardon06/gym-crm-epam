package trainer;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.usecase.trainer.RetrieveTrainerService;
import com.gym.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveTrainerServiceTest {
    @Mock AuthenticateUseCase authenticator;
    @Mock TrainerRepository trainerRepository;

    @InjectMocks
    RetrieveTrainerService service;

    AuthCredentials auth;
    Trainer trainer;

    @BeforeEach
    void setUp() {
        auth = new AuthCredentials("Coach.Temur", "123");
        trainer = new Trainer();
        trainer.setUsername("Coach.Temur");
    }

    @Test
    void returns_trainer_by_username() {
        when(trainerRepository.findByUsername("Coach.Temur")).thenReturn(Optional.of(trainer));
        Trainer result = service.getTrainer(auth, "Coach.Temur");
        assertThat(result.getUsername()).isEqualTo("Coach.Temur");
    }

    @Test
    void throws_when_not_found() {
        when(trainerRepository.findByUsername("Missing.User")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getTrainer(auth, "Missing.User"))
                .isInstanceOf(NotFoundException.class);
    }
}