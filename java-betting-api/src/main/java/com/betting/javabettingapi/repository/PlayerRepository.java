package com.betting.javabettingapi.repository;

import com.betting.javabettingapi.model.PlayerModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<PlayerModel, Long> {
    public Optional<PlayerModel> findByUsername(String username);
}
