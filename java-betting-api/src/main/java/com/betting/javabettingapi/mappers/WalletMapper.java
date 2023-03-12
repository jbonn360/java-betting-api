package com.betting.javabettingapi.mappers;

import com.betting.javabettingapi.dto.WalletDto;
import com.betting.javabettingapi.model.WalletModel;
import org.mapstruct.Mapper;

@Mapper
public interface WalletMapper {
    WalletDto walletModelToWalletDto(WalletModel walletModel);
    WalletModel walletDtoToWalletModel(WalletDto playerDto);
}
