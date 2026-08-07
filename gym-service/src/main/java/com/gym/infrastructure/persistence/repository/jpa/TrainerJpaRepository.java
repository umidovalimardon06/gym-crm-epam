package com.gym.infrastructure.persistence.repository.jpa;

import com.gym.infrastructure.persistence.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TrainerJpaRepository extends JpaRepository<TrainerEntity, Long> {

    Optional<TrainerEntity> findByUser_Username(String username);
    boolean existsByUser_Username(String username);

    @Query("""
           SELECT tr FROM TrainerEntity tr
           WHERE tr.id NOT IN (
                SELECT t.id FROM TraineeEntity te JOIN te.trainers t
                WHERE te.user.username = :traineeUsername
           )
           AND tr.user.isActive = true
           """)
    List<TrainerEntity> findTrainersNotAssignedToTrainee(String traineeUsername);

    Optional<TrainerEntity> findByUser_Id(Long userId);

    @Query("""
    SELECT t FROM TrainerEntity t
    WHERE t.user.isActive = true
      AND NOT EXISTS (
        SELECT 1 FROM TraineeEntity te
        JOIN te.trainers tr
        WHERE te.user.username = :traineeUsername
          AND tr.id = t.id
      )
""")
    List<TrainerEntity> findUnassignedActiveTrainersForTrainee(
            @Param("traineeUsername") String traineeUsername
    );

    List<TrainerEntity> findAllByUser_IsActiveTrue();

    List<TrainerEntity> findAllByUser_IdIn(Collection<Long> userIds);

    @Override
    Optional<TrainerEntity> findById(Long aLong);
}