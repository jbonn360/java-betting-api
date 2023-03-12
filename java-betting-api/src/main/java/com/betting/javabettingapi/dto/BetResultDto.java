package com.betting.javabettingapi.dto;

import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Currency;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Builder
@Data
public class BetResultDto {
    private String gameActivityId;

    private BetStatus outcome;

    private BigDecimal winAmount;

    private Currency currency;

    private BigDecimal playerBalanceAfter;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal betAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gameId;
}
