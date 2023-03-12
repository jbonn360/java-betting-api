package com.betting.javabettingapi.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
public class PlayerDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 12)
    private String username;
    private WalletDto wallet;
}
