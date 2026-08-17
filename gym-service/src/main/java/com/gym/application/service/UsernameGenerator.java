package com.gym.application.service;

import com.gym.application.port.output.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    private final UserRepository userRepository;

    public UsernameGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generate(String firstName, String lastName) {
        String base = firstName.toLowerCase() + "." + lastName.toLowerCase();

        if (!userRepository.existsByUsername(base)) {
            return base;
        }

        int suffix = 1;
        while (userRepository.existsByUsername(base + suffix)) {
            suffix++;
        }
        return base + suffix;
    }
}