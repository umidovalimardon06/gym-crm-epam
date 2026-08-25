package com.epam.workload.repository;

import com.epam.workload.domain.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerWorkloadRepository
        extends MongoRepository<TrainerWorkload, String> {

    Optional<TrainerWorkload> findByUsername(String username);
}