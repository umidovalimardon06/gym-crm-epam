package com.gym.infrastructure.persistence.repository.adapter;

import com.gym.application.port.output.TraineeRepository;
import com.gym.domain.Trainee;
import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import com.gym.infrastructure.persistence.entity.UserEntity;
import com.gym.infrastructure.persistence.mapper.TraineeMapper;
import com.gym.infrastructure.persistence.repository.jpa.TraineeJpaRepository;
import com.gym.infrastructure.persistence.repository.jpa.TrainerJpaRepository;
import com.gym.infrastructure.persistence.repository.jpa.TrainingJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class TraineeRepositoryAdapter implements TraineeRepository {

    private final TraineeJpaRepository jpa;
    private final TrainerJpaRepository trainerJpa;
    private final TraineeMapper mapper;
    private final TrainingJpaRepository trainingJpa;

    public TraineeRepositoryAdapter(TraineeJpaRepository jpa,
                                    TrainerJpaRepository trainerJpa,
                                    TrainingJpaRepository trainingJpa,
                                    TraineeMapper mapper) {
        this.jpa = jpa;
        this.trainerJpa = trainerJpa;
        this.trainingJpa = trainingJpa;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Trainee save(Trainee trainee) {
        if (trainee.getUserId() == null) {
            return mapper.toDomain(jpa.save(mapper.toEntity(trainee)));
        }

        TraineeEntity existing = jpa.findByUser_Id(trainee.getUserId())
                .orElseThrow(() -> new IllegalStateException(
                        "Trainee not found for update: userId=" + trainee.getUserId()));

        UserEntity user = existing.getUser();
        user.setFirstName(trainee.getFirstName());
        user.setLastName(trainee.getLastName());
        user.setUsername(trainee.getUsername());
        user.setPassword(trainee.getPassword());
        user.setActive(trainee.isActive());

        existing.setDateOfBirth(trainee.getDateOfBirth());
        existing.setAddress(trainee.getAddress());

        return mapper.toDomain(jpa.save(existing));
    }

    @Override
    @Transactional
    public Trainee updateTrainers(String traineeUsername, List<Long> trainerUserIds) {
        TraineeEntity trainee = jpa.findByUser_Username(traineeUsername)
                .orElseThrow(() -> new IllegalStateException(
                        "Trainee not found: " + traineeUsername));

        List<TrainerEntity> newTrainers = trainerJpa.findAllByUser_IdIn(trainerUserIds);
        if (newTrainers.size() != trainerUserIds.size()) {
            throw new IllegalArgumentException(
                    "One or more trainer ids not found: " + trainerUserIds);
        }

        trainee.setTrainers(new HashSet<>(newTrainers));
        return mapper.toDomain(jpa.save(trainee));
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        TraineeEntity trainee = jpa.findByUser_Username(username)
                .orElseThrow(() -> new IllegalStateException(
                        "Trainee not found: " + username));

        trainingJpa.deleteAllByTrainee_Id(trainee.getId());
        jpa.delete(trainee);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return jpa.findByUser_Username(username).map(mapper::toDomain);
    }
    @Override
    public Optional<Trainee> findById(Long userId) {
        return jpa.findByUser_Id(userId).map(mapper::toDomain);
    }

    @Override
    public List<Trainee> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }
    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUser_Username(username);
    }
}