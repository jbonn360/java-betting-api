package com.betting.javabettingapi.dto;

import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BetResultDto {
    private Long gameActivityId;

    private BetStatus outcome;

    private BigDecimal winAmount;

    private Currency currency;

    private BigDecimal playerBalanceAfter;
}
