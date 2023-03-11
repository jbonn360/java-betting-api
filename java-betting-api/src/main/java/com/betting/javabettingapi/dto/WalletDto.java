package com.betting.javabettingapi.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletDto {
    private Long id;

    private BigDecimal balance;
}
