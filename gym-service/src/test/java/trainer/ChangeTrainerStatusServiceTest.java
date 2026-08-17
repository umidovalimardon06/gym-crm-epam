package trainer;

import com.gym.application.exception.InvalidStateException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.usecase.trainer.ChangeTrainerStatusService;
import com.gym.domain.Trainer;
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
class ChangeTrainerStatusServiceTest {
    @Mock AuthenticateUseCase authenticator;
    @Mock TrainerRepository trainerRepository;
    @InjectMocks
    ChangeTrainerStatusService service;

    AuthCredentials auth;
    Trainer trainer;

    @BeforeEach
    void setUp() {
        auth = new AuthCredentials("Coach.Temur", "123");
        trainer = new Trainer();
        trainer.setUsername("Coach.Temur");
        trainer.setActive(true);
    }

    @Test
    void deactivates_active_trainer() {
        when(trainerRepository.findByUsername("Coach.Temur")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        service.deactivate(auth);

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository).save(captor.capture());
        assertThat(captor.getValue().isActive()).isFalse();
    }

    @Test
    void throws_when_already_inactive() {
        trainer.setActive(false);
        when(trainerRepository.findByUsername("Coach.Temur")).thenReturn(Optional.of(trainer));

        assertThatThrownBy(() -> service.deactivate(auth))
                .isInstanceOf(InvalidStateException.class);
    }
}