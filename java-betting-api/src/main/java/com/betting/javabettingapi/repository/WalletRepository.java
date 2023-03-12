package com.betting.javabettingapi.repository;

import com.betting.javabettingapi.model.WalletModel;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<WalletModel, Long> {
}

