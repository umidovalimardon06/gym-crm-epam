package com.gym.application.service;

import com.gym.application.port.output.UsernameRepository;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    private final UsernameRepository usernameRepository;

    public UsernameGenerator(UsernameRepository usernameRepository) {
        this.usernameRepository = usernameRepository;
    }

    public String generate(String firstName, String lastName) {
        String base = firstName + "." + lastName;

        if (!usernameRepository.existsByUsername(base)) {
            return base;
        }

        int suffix = 1;
        while (usernameRepository.existsByUsername(base + suffix)) {
            suffix++;
        }
        return base + suffix;
    }
}