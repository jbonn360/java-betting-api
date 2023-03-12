package com.betting.javabettingapi.mappers;

import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.model.GameActivityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BetResultMapper {
    @Mapping(target = "gameId", source = "gameActivityModel.game.id")
    BetResultDto gameActivityModelToBetResultDto(GameActivityModel gameActivityModel);
}