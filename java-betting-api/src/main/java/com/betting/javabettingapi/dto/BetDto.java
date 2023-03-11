package com.betting.javabettingapi.dto;

import com.betting.javabettingapi.utils.Currency;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class BetDto {
    @NotNull
    @Positive
    private Long gameActivityId;

    @Positive
    private BigDecimal betAmount;

    @NotNull
    private Currency currency;

    @NotNull
    @Positive
    private Long playerId;

    @NotNull
    @Positive
    private Integer gameId;
}
