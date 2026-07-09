package com.gym.infrastructure.persistence.entity;

import com.gym.domain.TrainingType;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "trainings")
public class TrainingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trainee_id", nullable = false)
    private TraineeEntity trainee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    private TrainerEntity trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_type", nullable = false)
    private TrainingType trainingType;

    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration_minutes", nullable = false)
    private Integer trainingDurationMinutes;

    protected TrainingEntity() {}

    public TrainingEntity(TraineeEntity trainee, TrainerEntity trainer, String trainingName,
                          TrainingType trainingType, LocalDate trainingDate,
                          Integer trainingDurationMinutes) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDurationMinutes = trainingDurationMinutes;
    }

    public Long getId() { return id; }
    public TraineeEntity getTrainee() { return trainee; }
    public TrainerEntity getTrainer() { return trainer; }
    public String getTrainingName() { return trainingName; }
    public TrainingType getTrainingType() { return trainingType; }
    public LocalDate getTrainingDate() { return trainingDate; }
    public Integer getTrainingDurationMinutes() { return trainingDurationMinutes; }
}