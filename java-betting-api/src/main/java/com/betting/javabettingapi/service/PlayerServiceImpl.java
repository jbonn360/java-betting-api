package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.PlayerDto;
import com.betting.javabettingapi.exception.EntityNotFoundException;
import com.betting.javabettingapi.exception.UsernameTakenException;
import com.betting.javabettingapi.mappers.PlayerMapper;
import com.betting.javabettingapi.model.PlayerModel;
import com.betting.javabettingapi.model.WalletModel;
import com.betting.javabettingapi.repository.PlayerRepository;
import com.betting.javabettingapi.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
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
                        String.format("Player with id %d was not found", playerId)
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
}
