package com.betting.javabettingapi.mappers;

import com.betting.javabettingapi.dto.PlayerDto;
import com.betting.javabettingapi.model.PlayerModel;
import org.mapstruct.Mapper;


@Mapper
public interface PlayerMapper {
    PlayerDto playerModelToPlayerDto(PlayerModel playerModel);
    PlayerModel playerDtoToPlayerModel(PlayerDto playerDto);
}
