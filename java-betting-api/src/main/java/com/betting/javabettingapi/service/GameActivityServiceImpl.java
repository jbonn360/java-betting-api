package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.dto.BetResultListDto;
import com.betting.javabettingapi.exception.GameActivityIdExistsException;
import com.betting.javabettingapi.exception.InsufficientFundsException;
import com.betting.javabettingapi.mappers.BetResultMapper;
import com.betting.javabettingapi.model.GameActivityModel;
import com.betting.javabettingapi.model.GameModel;
import com.betting.javabettingapi.model.PlayerModel;
import com.betting.javabettingapi.model.WalletModel;
import com.betting.javabettingapi.repository.GameActivityRepository;
import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameActivityServiceImpl implements GameActivityService {
    private final GameService gameService;
    private final PlayerService playerService;
    private final GameActivityRepository gameActivityRepository;
    private final BetResultMapper betMapper;
    private final Utils utils;
    private final BigDecimal prize;

    public GameActivityServiceImpl(
            @Autowired GameService gameService,
            @Autowired PlayerService playerService,
            @Autowired GameActivityRepository gameActivityRepository,
            @Autowired BetResultMapper betMapper,
            @Autowired Utils utils,
            @Value("${app.game.prize}") BigDecimal prize) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.gameActivityRepository = gameActivityRepository;
        this.betMapper = betMapper;
        this.utils = utils;
        this.prize = prize;
    }

    @Override
    public BetResultListDto getAllBets(int limit, int offset) {
        List<GameActivityModel> gameActivityList =
                gameActivityRepository.findAllByOrderByIdAsc(PageRequest.of(offset, limit));

        // map game activity model to bet result dto
        List<BetResultDto> betResults = gameActivityList.stream().map(
                betMapper::gameActivityModelToBetResultDto
        ).collect(Collectors.toList());

        BetResultListDto result = new BetResultListDto(betResults);

        return result;
    }

    @Transactional
    @Override
    public BetResultDto submitBet(BetDto betDto) {
        // ensure that game activity id is unique
        ensureGameActivityIdUnique(betDto.getGameActivityId());

        // get player from database
        PlayerModel player = playerService.getPlayerModelById(betDto.getPlayerId());

        // check that player affords the bet
        if (!playerService.affordsAmount(player.getId(), betDto.getBetAmount()))
            throw new InsufficientFundsException("Wallet does not have enough funds");

        // get referenced game
        GameModel game = gameService.getGameModelById(betDto.getGameId());

        // generate a random number to decide if game is won or lost
        boolean gameWon = utils.generateRandomBoolean();

        // update player's wallet balance
        WalletModel walletUpdated = updateWalletBalance(gameWon, player.getId(), betDto.getBetAmount());

        // update game activity model
        GameActivityModel gameActivitySaved = createGameActivityModel(
                betDto, gameWon, player, game, walletUpdated.getBalance());

        // put together bet result dto object and return it
        return createBetResult(gameActivitySaved);
    }

    private GameActivityModel createGameActivityModel(
            BetDto betDto, boolean gameWon, PlayerModel player, GameModel game, BigDecimal newBalance) {
        final GameActivityModel gameActivity = GameActivityModel.builder()
                .gameActivityId(betDto.getGameActivityId())
                .betAmount(betDto.getBetAmount())
                .currency(betDto.getCurrency())
                .player(player)
                .game(game)
                .playerBalanceAfter(newBalance).build();

        // update game outcome
        if (gameWon) {
            gameActivity.setOutcome(BetStatus.WIN);
            gameActivity.setWinAmount(betDto.getBetAmount().add(prize));
        } else {
            gameActivity.setOutcome(BetStatus.LOSS);
            gameActivity.setWinAmount(BigDecimal.ZERO.subtract(betDto.getBetAmount()));
        }

        //persist updated game activity entity in database
        return gameActivityRepository.save(gameActivity);
    }

    private WalletModel updateWalletBalance(boolean gameWon, Long playerId, BigDecimal betAmount) {
        BigDecimal amountToAdd = BigDecimal.ZERO;

        amountToAdd = gameWon ? prize : amountToAdd.subtract(betAmount);

        return playerService.addAmountToPlayerWalletBalance(playerId, amountToAdd);
    }

    private BetResultDto createBetResult(GameActivityModel gameActivity) {
        BetResultDto resultDto = betMapper.gameActivityModelToBetResultDto(gameActivity);
        resultDto.setGameId(null);
        resultDto.setBetAmount(null);

        return resultDto;
    }

    private void ensureGameActivityIdUnique(Long gameActivityId) {
        if (gameActivityRepository.findByGameActivityId(gameActivityId).isPresent())
            throw new GameActivityIdExistsException(
                    String.format("Game activity id %d already exists", gameActivityId)
            );
    }
}
