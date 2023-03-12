package com.betting.javabettingapi.repository;

import com.betting.javabettingapi.model.GameModel;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<GameModel, Integer> {
}
