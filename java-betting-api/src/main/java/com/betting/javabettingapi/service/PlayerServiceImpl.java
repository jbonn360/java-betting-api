package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.PlayerDto;
import com.betting.javabettingapi.exception.EntityNotFoundException;
import com.betting.javabettingapi.exception.InsufficientFundsException;
import com.betting.javabettingapi.exception.InvalidTransactionException;
import com.betting.javabettingapi.exception.UsernameTakenException;
import com.betting.javabettingapi.mappers.PlayerMapper;
import com.betting.javabettingapi.model.PlayerModel;
import com.betting.javabettingapi.model.WalletModel;
import com.betting.javabettingapi.repository.PlayerRepository;
import com.betting.javabettingapi.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final WalletRepository walletRepository;
    private final PlayerMapper playerMapper;
    private final BigDecimal startingBalance;

    public PlayerServiceImpl(
            @Autowired PlayerRepository playerRepository,
            @Autowired WalletRepository walletRepository,
            @Autowired PlayerMapper playerMapper,
            @Value("${app.wallet.starting-balance}") BigDecimal startingBalance)
    {
        this.playerRepository = playerRepository;
        this.walletRepository = walletRepository;
        this.playerMapper = playerMapper;
        this.startingBalance = startingBalance;
    }

    @Override
    public PlayerDto getPlayerById(Long playerId) {
        PlayerModel player = playerRepository.findById(playerId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Player with id %d was not found.", playerId)
                )
        );

        PlayerDto playerDto = playerMapper.playerModelToPlayerDto(player);

        return playerDto;
    }

    @Transactional
    @Override
    public Long createPlayerAccount(PlayerDto playerDto) {
        if(playerRepository.findByUsername(playerDto.getUsername()).isPresent())
            throw new UsernameTakenException("This username already exists. Please choose a new one.");

        WalletModel walletModel = new WalletModel(-1l, null, startingBalance);
        WalletModel walletModelSaved = walletRepository.save(walletModel);

        PlayerModel playerModel = playerMapper.playerDtoToPlayerModel(playerDto);
        playerModel.setWallet(walletModelSaved);
        PlayerModel playerModelSaved = playerRepository.save(playerModel);

        walletModelSaved.setPlayer(playerModelSaved);
        walletModelSaved = walletRepository.save(walletModelSaved);

        return playerModelSaved.getId();
    }

    @Override
    public WalletModel addAmountToPlayerWalletBalance(Long playerId, BigDecimal amount) {
        if(amount.equals(BigDecimal.ZERO))
            throw new InvalidTransactionException("Can't add zero to player balance");

        PlayerModel player = playerRepository.findById(playerId).orElseThrow(
                () -> new EntityNotFoundException("Player not found in database")
        );

        BigDecimal newBalance = player.getWallet().getBalance().add(amount);

        if(newBalance.compareTo(BigDecimal.ZERO) <= -1)
            throw new InsufficientFundsException("Wallet does not have enough funds");

        player.getWallet().setBalance(newBalance);

        return walletRepository.save(player.getWallet());
    }

    @Override
    public boolean affordsAmount(Long playerId, BigDecimal amount) {
        PlayerModel player = playerRepository.findById(playerId).orElseThrow(
                () -> new EntityNotFoundException("Player not found in database")
        );

        BigDecimal newBalance = player.getWallet().getBalance().subtract(amount);

        // if subtracting the amount from the current balance would make
        // it less than zero
        if(newBalance.compareTo(BigDecimal.ZERO) <= -1)
            return false;
        else
            return true;
    }


}
