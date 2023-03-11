package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;

public interface GameActivityService {
    BetResultDto submitBet(BetDto betDto);
}
