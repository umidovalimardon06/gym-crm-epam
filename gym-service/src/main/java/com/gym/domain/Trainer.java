package com.gym.domain;

import java.util.Objects;

public class Trainer extends User {
    private TrainingType specialization;

    public Trainer() {
        super();
    }

    public Trainer(Long userId,
                   String firstName,
                   String lastName,
                   String username,
                   String password,
                   TrainingType specialization) {
        super(userId, firstName, lastName, username, password);
        this.specialization = specialization;
    }


    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer)) return false;
        if (!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return specialization == trainer.specialization;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialization);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                super.toString() +
                ", specialization=" + specialization +
                '}';
    }
}