package com.gym.infrastructure.secuirty;

import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GymUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(GymUserDetailsService.class);

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public GymUserDetailsService(TraineeRepository traineeRepository,
                                 TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String role = "ROLE_TRAINEE";
        User user = traineeRepository.findByUsername(username)
                .map(t -> (User) t)
                .orElse(null);

        if (user == null) {
            log.debug("User {} not found as trainee, searching in trainer repository", username);
            role = "ROLE_TRAINER";
            user = trainerRepository.findByUsername(username)
                    .map(t -> (User) t)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found: " + username));
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(role))
                .disabled(!user.isActive())
                .build();
    }
}
