package com.gym.application.usecase.util;

import java.util.Set;

public class UsernameGenerator {
    public String generateUsername(String firstName, String lastName, Set<String> existing) {
        String base = firstName + "." + lastName;

        if (!existing.contains(base)) {
            return base;
        }

        int suffix = 1;
        while (existing.contains(base + suffix)) {
            suffix++;
        }
        return base + suffix;
    }
}