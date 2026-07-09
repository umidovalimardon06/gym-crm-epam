package com.gym.infrastructure.persistence.entity;

import com.gym.domain.TrainingType;
import jakarta.persistence.*;

@Entity
@Table(name = "trainers")
public class TrainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    protected TrainerEntity() {}

    public TrainerEntity(TrainingType specialization, UserEntity user) {
        this.specialization = specialization;
        this.user = user;
    }

    public Long getId() { return id; }
    public TrainingType getSpecialization() { return specialization; }
    public UserEntity getUser() { return user; }

    public void setSpecialization(TrainingType specialization) { this.specialization = specialization; }
    public void setUser(UserEntity user) { this.user = user; }
}