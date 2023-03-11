package com.betting.javabettingapi.repository;

import com.betting.javabettingapi.model.PlayerModel;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<PlayerModel, Long> {
}
