package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.dto.BetResultListDto;

public interface GameActivityService {
    BetResultDto submitBet(BetDto betDto);

    BetResultListDto getAllBets(int limit, int offset);
}
