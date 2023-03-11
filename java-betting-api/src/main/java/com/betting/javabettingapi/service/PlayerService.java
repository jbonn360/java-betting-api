package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.PlayerDto;

public interface PlayerService {
    PlayerDto getPlayerById(Long playerId);

    Long createPlayerAccount(PlayerDto playerDto);
}
