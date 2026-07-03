package trainer;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.usecase.trainer.UpdateTrainerService;
import com.gym.domain.Trainer;
import com.gym.domain.TrainingType;
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
class UpdateTrainerServiceTest {
    @Mock AuthenticateUseCase authenticator;
    @Mock TrainerRepository trainerRepository;

    @InjectMocks
    UpdateTrainerService service;

    AuthCredentials auth;
    Trainer existing;

    @BeforeEach
    void setUp() {
        auth = new AuthCredentials("Coach.Temur", "123");
        existing = new Trainer();
        existing.setUsername("Coach.Temur");
        existing.setFirstName("Coach");
        existing.setLastName("Temur");
        existing.setSpecialization(TrainingType.CARDIO);
        existing.setActive(true);
    }

    @Test
    void updates_trainer_fields() {
        Trainer patch = new Trainer();
        patch.setSpecialization(TrainingType.STRENGTH);
        patch.setActive(true);

        when(trainerRepository.findByUsername("Coach.Temur")).thenReturn(Optional.of(existing));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        service.updateTrainerProfile(auth, patch);

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository).save(captor.capture());
        assertThat(captor.getValue().getSpecialization()).isEqualTo(TrainingType.STRENGTH);
    }

    @Test
    void throws_when_not_found() {
        when(trainerRepository.findByUsername("Coach.Temur")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateTrainerProfile(auth, new Trainer()))
                .isInstanceOf(NotFoundException.class);
    }
}