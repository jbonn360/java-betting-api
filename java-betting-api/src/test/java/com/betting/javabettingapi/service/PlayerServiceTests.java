package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.PlayerDto;
import com.betting.javabettingapi.mappers.PlayerMapper;
import com.betting.javabettingapi.model.PlayerModel;
import com.betting.javabettingapi.model.WalletModel;
import com.betting.javabettingapi.repository.PlayerRepository;
import com.betting.javabettingapi.repository.WalletRepository;
import com.betting.javabettingapi.utils.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayerServiceTests {
    private PlayerRepository playerRepository = mock(PlayerRepository.class);
    private WalletRepository walletRepository = mock(WalletRepository.class);
    private PlayerMapper playerMapper = mock(PlayerMapper.class);
    private BigDecimal startingBalance = new BigDecimal(2000);

    private PlayerServiceImpl playerService = new PlayerServiceImpl(
            playerRepository, walletRepository, playerMapper, startingBalance
    );

    @Test
    public void givenCallToCreatePlayerAccount_WhenPlayerDtoIsValid_ThenCreatePlayerAndReturnId(){
        //given
        PlayerDto playerDto = PlayerDto.builder().username("username").build();
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUsername("username");

        WalletModel walletModel = new WalletModel();
        PlayerModel playerCreated = new PlayerModel(1l, "username", walletModel);

        when(walletRepository.save(any())).thenReturn(walletModel);
        when(playerMapper.playerDtoToPlayerModel(playerDto)).thenReturn(playerModel);
        when(playerRepository.save(playerModel)).thenReturn(playerCreated);

        //when
        Long playerId = playerService.createPlayerAccount(playerDto);

        //then
        assertEquals(playerCreated.getId(), playerId);
    }

    @Test
    public void givenCallToAddAmountToWalletBalance_WhenAmountISValid_ThenAmountIsAdded(){
        //given
        BigDecimal amountToAdd = new BigDecimal(100);
        BigDecimal initialBalance = new BigDecimal(50);
        WalletModel wallet = new WalletModel(1l, initialBalance, Currency.EUR);
        PlayerModel player = new PlayerModel(1l, "username", wallet);

        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        WalletModel walletSaved = playerService.addAmountToPlayerWalletBalance(1l, amountToAdd);

        //then
        assertEquals(initialBalance.add(amountToAdd), walletSaved.getBalance());
    }

    @Test
    public void givenCallToAffordsAmount_WhenAmountIsLessThanBalance_ThenReturnTrue(){
        //given
        WalletModel wallet = new WalletModel(1l, new BigDecimal(200), Currency.EUR);
        PlayerModel player = new PlayerModel(1l, "username", wallet);
        when(playerRepository.findById(1l)).thenReturn(Optional.of(player));

        //when
        boolean result = playerService.affordsAmount(1l, new BigDecimal(100));

        //then
        assertEquals(true, result);
    }

    @Test
    public void givenCallToAffordsAmount_WhenAmountIsMoreThanBalance_ThenReturnFalse(){
        //given
        WalletModel wallet = new WalletModel(1l, new BigDecimal(5), Currency.EUR);
        PlayerModel player = new PlayerModel(1l, "username", wallet);
        when(playerRepository.findById(1l)).thenReturn(Optional.of(player));

        //when
        boolean result = playerService.affordsAmount(1l, new BigDecimal(100));

        //then
        assertEquals(false, result);
    }
}
