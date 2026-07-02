package com.gym.infrastructure.persistence.repository.jpa;

import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TraineeJpaRepository extends JpaRepository<TraineeEntity, Long> {
    Optional<TraineeEntity> findByUser_Username(String username);
    boolean existsByUser_Username(String username);
    void deleteByUser_Username(String username);
    Optional<TraineeEntity> findByUser_Id(Long userId);
}