package com.betting.javabettingapi.repository;

import com.betting.javabettingapi.model.GameActivityModel;
import org.springframework.data.repository.CrudRepository;

public interface GameActivityRepository extends CrudRepository<GameActivityModel, Long> {
}
