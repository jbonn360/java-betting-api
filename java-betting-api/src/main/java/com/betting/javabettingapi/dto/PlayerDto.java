package com.betting.javabettingapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDto {
    private Long id;
    private String username;
    private WalletDto wallet;
}
