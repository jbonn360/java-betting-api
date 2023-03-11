package com.betting.javabettingapi.dto;

import com.betting.javabettingapi.utils.Currency;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
public class WalletDto {

    private Long id;

    private BigDecimal balance;

    private Currency currency;
}
