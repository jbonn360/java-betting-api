package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.exception.EntityNotFoundException;
import com.betting.javabettingapi.exception.InsufficientFundsException;
import com.betting.javabettingapi.exception.InvalidPlayerException;
import com.betting.javabettingapi.model.GameActivityModel;
import com.betting.javabettingapi.model.GameModel;
import com.betting.javabettingapi.model.WalletModel;
import com.betting.javabettingapi.repository.GameActivityRepository;
import com.betting.javabettingapi.repository.GameRepository;
import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class GameActivityServiceImpl implements GameActivityService {
    private final GameRepository gameRepository;
    private final GameActivityRepository gameActivityRepository;
    private final PlayerService playerService;
    private final Utils utils;
    private final BigDecimal prize;

    public GameActivityServiceImpl(
            @Autowired GameRepository gameRepository,
            @Autowired GameActivityRepository gameActivityRepository,
            @Autowired PlayerService playerService,
            @Autowired Utils utils,
            @Value("${app.game.prize}") BigDecimal prize) {
        this.gameRepository = gameRepository;
        this.gameActivityRepository = gameActivityRepository;
        this.playerService = playerService;
        this.utils = utils;
        this.prize = prize;
    }

    @Transactional
    @Override
    public BetResultDto submitBet(BetDto betDto) {
        // locate pre-existing game activity entry in database
        GameActivityModel gameActivity = gameActivityRepository.findById(betDto.getGameActivityId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Game activity with id %d was not found.",
                        betDto.getGameActivityId()))
        );

        // make sure player id matches the one referenced by game activity entity
        if (!gameActivity.getPlayer().getId().equals(betDto.getPlayerId()))
            throw new InvalidPlayerException(String.format("Game activity was created by player %d but " +
                    "player id in this request was %d", gameActivity.getPlayer().getId(), betDto.getPlayerId()));

        // check that player affords the bet
        if (!playerService.affordsAmount(betDto.getPlayerId(), betDto.getBetAmount()))
            throw new InsufficientFundsException("Wallet does not have enough funds");

        // generate a random number to decide if game is won or lost
        boolean gameWon = utils.generateRandomBoolean();

        // update game activity model
        updateGameActivityModel(gameActivity, betDto, gameWon);

        // update player's wallet balance
        WalletModel walletUpdated = updateWalletBalance(gameWon, betDto.getPlayerId(), betDto.getBetAmount());

        // put together bet result dto object and return it
        return createBetResult(gameActivity, walletUpdated.getBalance());
    }

    private void updateGameActivityModel(GameActivityModel gameActivity, BetDto betDto, boolean gameWon) {
        // get referenced game type from database
        GameModel gameModel = gameRepository.findById(betDto.getGameId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Game entry with id %d was not found.",
                        betDto.getGameActivityId()))
        );

        // update game properties: game instance, bet currency, and bet amount
        gameActivity.setGame(gameModel);
        gameActivity.setCurrency(betDto.getCurrency());
        gameActivity.setBetAmount(betDto.getBetAmount());

        // update game outcome
        if (gameWon) {
            gameActivity.setBetStatus(BetStatus.WON);
            gameActivity.setAmountWon(betDto.getBetAmount().add(prize));
        } else {
            gameActivity.setBetStatus(BetStatus.LOST);
            gameActivity.setAmountWon(BigDecimal.ZERO);
        }

        //persist updated game activity entity in database
        gameActivityRepository.save(gameActivity);
    }

    private WalletModel updateWalletBalance(boolean gameWon, Long playerId, BigDecimal betAmount) {
        BigDecimal amountToAdd = BigDecimal.ZERO;

        amountToAdd = gameWon ? prize : amountToAdd.subtract(betAmount);

        return playerService.addAmountToPlayerWalletBalance(playerId, amountToAdd);
    }

    private BetResultDto createBetResult(GameActivityModel gameActivity, BigDecimal balanceAfter) {
        BetResultDto resultDto = BetResultDto.builder()
                .gameActivityId(gameActivity.getId())
                .outcome(gameActivity.getBetStatus())
                .winAmount(gameActivity.getAmountWon())
                .currency(gameActivity.getCurrency())
                .playerBalanceAfter(balanceAfter).build();

        return resultDto;
    }
}
