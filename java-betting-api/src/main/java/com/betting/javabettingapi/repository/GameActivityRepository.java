package com.betting.javabettingapi.repository;

import com.betting.javabettingapi.model.GameActivityModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GameActivityRepository extends CrudRepository<GameActivityModel, Long> {
    Optional<GameActivityModel> findByGameActivityId(Long gameActivityId);
}
