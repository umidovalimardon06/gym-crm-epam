package com.gym.infrastructure.persistence.repository.jpa;

import com.gym.domain.TrainingType;
import com.gym.infrastructure.persistence.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingJpaRepository extends JpaRepository<TrainingEntity, Long> {

    @Query("""
            SELECT t FROM TrainingEntity t
            WHERE t.trainee.user.username = :username
              AND (CAST(:from AS date) IS NULL OR t.trainingDate >= :from)
              AND (CAST(:to   AS date) IS NULL OR t.trainingDate <= :to)
              AND (CAST(:trainerName AS string) IS NULL
                   OR LOWER(t.trainer.user.firstName) LIKE LOWER(CONCAT('%', CAST(:trainerName AS string), '%'))
                   OR LOWER(t.trainer.user.lastName)  LIKE LOWER(CONCAT('%', CAST(:trainerName AS string), '%')))
              AND (:type IS NULL OR t.trainingType = :type)
            ORDER BY t.trainingDate DESC
            """)
    List<TrainingEntity> findTraineeTrainings(
            @Param("username") String username,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("trainerName") String trainerName,
            @Param("type") TrainingType type
    );

    @Query("""
                SELECT t FROM TrainingEntity t
                WHERE t.trainer.user.username = :username
                  AND (CAST(:from AS date) IS NULL OR t.trainingDate >= :from)
                  AND (CAST(:to   AS date) IS NULL OR t.trainingDate <= :to)
                  AND (CAST(:traineeName AS string) IS NULL
                       OR LOWER(t.trainee.user.firstName) LIKE LOWER(CONCAT('%', CAST(:traineeName AS string), '%'))
                       OR LOWER(t.trainee.user.lastName)  LIKE LOWER(CONCAT('%', CAST(:traineeName AS string), '%')))
                ORDER BY t.trainingDate DESC
            """)
    List<TrainingEntity> findTrainerTrainings(
            @Param("username") String username,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("traineeName") String traineeName
    );
}