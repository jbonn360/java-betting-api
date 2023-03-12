package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.PlayerDto;
import com.betting.javabettingapi.model.PlayerModel;
import com.betting.javabettingapi.model.WalletModel;

import java.math.BigDecimal;

public interface PlayerService {
    PlayerDto getPlayerById(Long playerId);

    PlayerModel getPlayerModelById(Long playerId);

    Long createPlayerAccount(PlayerDto playerDto);

    WalletModel addAmountToPlayerWalletBalance(Long playerId, BigDecimal amount);

    boolean affordsAmount(Long playerId, BigDecimal amount);
}
