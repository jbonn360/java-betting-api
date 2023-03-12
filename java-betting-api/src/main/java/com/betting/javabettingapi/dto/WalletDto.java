package com.betting.javabettingapi.dto;

import com.betting.javabettingapi.utils.Currency;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Builder
@Data
public class WalletDto {
    private Long id;

    private BigDecimal balance;

    private Currency currency;
}
