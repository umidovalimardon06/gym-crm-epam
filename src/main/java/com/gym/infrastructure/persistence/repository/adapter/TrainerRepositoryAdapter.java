package com.gym.infrastructure.persistence.repository.adapter;

import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.Trainer;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import com.gym.infrastructure.persistence.entity.UserEntity;
import com.gym.infrastructure.persistence.mapper.TrainerMapper;
import com.gym.infrastructure.persistence.repository.jpa.TrainerJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepositoryAdapter implements TrainerRepository {

    private final TrainerJpaRepository jpa;
    private final TrainerMapper mapper;

    public TrainerRepositoryAdapter(TrainerJpaRepository jpa, TrainerMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public List<Trainer> findUnassignedActiveTrainersForTrainee(String traineeUsername) {
        return jpa.findUnassignedActiveTrainersForTrainee(traineeUsername)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public Trainer save(Trainer trainer) {
        if (trainer.getUserId() == null) {
            return mapper.toDomain(jpa.save(mapper.toEntity(trainer)));
        }

        TrainerEntity existing = jpa.findByUser_Id(trainer.getUserId())
                .orElseThrow(() -> new IllegalStateException(
                        "Trainer not found for update: userId=" + trainer.getUserId()));

        UserEntity user = existing.getUser();
        user.setFirstName(trainer.getFirstName());
        user.setLastName(trainer.getLastName());
        user.setUsername(trainer.getUsername());
        user.setPassword(trainer.getPassword());
        user.setActive(trainer.isActive());

        existing.setSpecialization(trainer.getSpecialization());

        return mapper.toDomain(jpa.save(existing));
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return jpa.findByUser_Username(username).map(mapper::toDomain);
    }

/*    @Override
    public List<Trainer> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername) {
        return jpa.findTrainersNotAssignedToTrainee(traineeUsername)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUser_Username(username);
    }*/
}