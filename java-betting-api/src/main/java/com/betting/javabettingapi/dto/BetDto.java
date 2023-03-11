package com.betting.javabettingapi.dto;

import com.betting.javabettingapi.utils.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BetDto {
    private Long gameActivityId;

    private BigDecimal betAmount;

    private Currency currency;

    private Long playerId;

    private Integer gameId;
}
