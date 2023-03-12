package com.betting.javabettingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class BetResultListDto {
    private List<BetResultDto> betResultList;
}
