package com.betting.javabettingapi.service;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.dto.BetResultListDto;
import com.betting.javabettingapi.mappers.BetResultMapper;
import com.betting.javabettingapi.mappers.PlayerMapper;
import com.betting.javabettingapi.model.GameActivityModel;
import com.betting.javabettingapi.model.GameModel;
import com.betting.javabettingapi.model.PlayerModel;
import com.betting.javabettingapi.model.WalletModel;
import com.betting.javabettingapi.repository.GameActivityRepository;
import com.betting.javabettingapi.repository.PlayerRepository;
import com.betting.javabettingapi.repository.WalletRepository;
import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Currency;
import com.betting.javabettingapi.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameActivityServiceTests {
    private final GameService gameService = mock(GameService.class);
    private final PlayerService playerService = mock(PlayerService.class);;
    private final GameActivityRepository gameActivityRepository = mock(GameActivityRepository.class);;
    private final BetResultMapper betMapper = mock(BetResultMapper.class);;
    private final Utils utils = mock(Utils.class);;
    private final BigDecimal prize = new BigDecimal(10);

    private final GameActivityServiceImpl gameActivityService = new GameActivityServiceImpl(
            gameService, playerService, gameActivityRepository, betMapper, utils,prize
    );

    @Test
    public void givenLimitAndOffset_whenGetAllBets_thenGetAllBets(){
        //given
        PlayerModel player = mock(PlayerModel.class);
        GameModel game = mock(GameModel.class);
        GameActivityModel gam1 = new GameActivityModel(
                1l, 1l, new BigDecimal(100), Currency.EUR, player, game,
                new BigDecimal(110), BetStatus.WIN, new BigDecimal(2110)
        );

        GameActivityModel gam2 = new GameActivityModel(
                2l, 2l, new BigDecimal(200), Currency.EUR, player, game,
                new BigDecimal(-200), BetStatus.LOSS, new BigDecimal(1800)
        );

        BetResultDto betRes1 = BetResultDto.builder().gameActivityId("1").outcome(BetStatus.WIN)
                .winAmount(new BigDecimal(110)).currency(Currency.EUR)
                .playerBalanceAfter(new BigDecimal(2110)).betAmount(new BigDecimal(100))
                .gameId("1").build();


        BetResultDto betRes2 = BetResultDto.builder().gameActivityId("2").outcome(BetStatus.LOSS)
                .winAmount(new BigDecimal(-200)).currency(Currency.EUR)
                .playerBalanceAfter(new BigDecimal(1800)).betAmount(new BigDecimal(200))
                .gameId("1").build();

        //when
        when(gameActivityRepository.findAllByOrderByIdAsc(any())).thenReturn(List.of(gam1, gam2));
        when(betMapper.gameActivityModelToBetResultDto(gam1)).thenReturn(betRes1);
        when(betMapper.gameActivityModelToBetResultDto(gam2)).thenReturn(betRes2);

        BetResultListDto returned = gameActivityService.getAllBets(1, 0);

        //then
        assertEquals(2, returned.getBetResultList().size());
        assertTrue(returned.getBetResultList().contains(betRes1));
        assertTrue(returned.getBetResultList().contains(betRes2));
    }

    @Test
    public void givenValidBetDto_whenBetIsWon_thenSubmitBetAndReturnResult(){
        //given
        BigDecimal startingBalance = new BigDecimal(2000);

        WalletModel walletModel = new WalletModel(1l, startingBalance, Currency.EUR);
        PlayerModel playerModel = new PlayerModel(1l, "username", walletModel);

        GameModel gameModel = new GameModel(1, "Game Name");
        BetDto betDto = BetDto.builder().gameActivityId(1l).betAmount(new BigDecimal(50))
                .currency(Currency.EUR).playerId(1l).gameId(1).build();

        BigDecimal playerBalanceAfter = walletModel.getBalance().add(prize);
        BigDecimal winAmount = betDto.getBetAmount().add(prize);

        final GameActivityModel gameActivity = GameActivityModel.builder()
                .gameActivityId(betDto.getGameActivityId())
                .betAmount(betDto.getBetAmount())
                .currency(betDto.getCurrency())
                .player(playerModel)
                .game(gameModel)
                .playerBalanceAfter(playerBalanceAfter)
                .outcome(BetStatus.WIN)
                .winAmount(winAmount).build();

        final BetResultDto betResult = BetResultDto.builder().gameActivityId(betDto.getGameActivityId().toString())
                .outcome(BetStatus.WIN).winAmount(gameActivity.getWinAmount()).currency(Currency.EUR)
                .playerBalanceAfter(gameActivity.getPlayerBalanceAfter()).betAmount(gameActivity.getBetAmount())
                .gameId(gameActivity.getGame().getId().toString()).build();

        //when
        when(gameActivityRepository.findByGameActivityId(betDto.getGameActivityId()))
                .thenReturn(Optional.empty());
        when(playerService.getPlayerModelById(betDto.getPlayerId())).thenReturn(playerModel);
        when(playerService.affordsAmount(playerModel.getId(), betDto.getBetAmount()))
                .thenReturn(true);
        when(gameService.getGameModelById(betDto.getGameId())).thenReturn(gameModel);
        when(utils.generateRandomBoolean()).thenReturn(true);
        when(playerService.addAmountToPlayerWalletBalance(playerModel.getId(), prize))
                .then(
                        a -> {
                            walletModel.setBalance(walletModel.getBalance().add(prize));
                            return walletModel;
                        }
                );

        when(gameActivityRepository.save(any())).thenReturn(gameActivity);
        when(betMapper.gameActivityModelToBetResultDto(any())).thenReturn(betResult);

        BetResultDto result = gameActivityService.submitBet(betDto);

        //then
        assertEquals(startingBalance.add(prize), walletModel.getBalance());
        assertEquals(betResult, result);
    }

    @Test
    public void givenValidBetDto_whenBetIsLost_thenSubmitBetAndReturnResult(){
        //given
        BigDecimal startingBalance = new BigDecimal(2000);

        WalletModel walletModel = new WalletModel(1l, startingBalance, Currency.EUR);
        PlayerModel playerModel = new PlayerModel(1l, "username", walletModel);

        GameModel gameModel = new GameModel(1, "Game Name");
        BetDto betDto = BetDto.builder().gameActivityId(1l).betAmount(new BigDecimal(100))
                .currency(Currency.EUR).playerId(1l).gameId(1).build();

        BigDecimal playerBalanceAfter = walletModel.getBalance().subtract(betDto.getBetAmount());
        BigDecimal winAmount = BigDecimal.ZERO.subtract(betDto.getBetAmount());

        final GameActivityModel gameActivity = GameActivityModel.builder()
                .gameActivityId(betDto.getGameActivityId())
                .betAmount(betDto.getBetAmount())
                .currency(betDto.getCurrency())
                .player(playerModel)
                .game(gameModel)
                .playerBalanceAfter(playerBalanceAfter)
                .outcome(BetStatus.LOSS)
                .winAmount(winAmount).build();

        final BetResultDto betResult = BetResultDto.builder().gameActivityId(betDto.getGameActivityId().toString())
                .outcome(BetStatus.LOSS).winAmount(gameActivity.getWinAmount()).currency(Currency.EUR)
                .playerBalanceAfter(gameActivity.getPlayerBalanceAfter()).betAmount(gameActivity.getBetAmount())
                .gameId(gameActivity.getGame().getId().toString()).build();

        //when
        when(gameActivityRepository.findByGameActivityId(betDto.getGameActivityId()))
                .thenReturn(Optional.empty());
        when(playerService.getPlayerModelById(betDto.getPlayerId())).thenReturn(playerModel);
        when(playerService.affordsAmount(playerModel.getId(), betDto.getBetAmount()))
                .thenReturn(true);
        when(gameService.getGameModelById(betDto.getGameId())).thenReturn(gameModel);
        when(utils.generateRandomBoolean()).thenReturn(false);
        when(playerService.addAmountToPlayerWalletBalance(playerModel.getId(), winAmount))
                .then(
                        a -> {
                            walletModel.setBalance(walletModel.getBalance().subtract(betDto.getBetAmount()));
                            return walletModel;
                        }
                );

        when(gameActivityRepository.save(any())).thenReturn(gameActivity);
        when(betMapper.gameActivityModelToBetResultDto(any())).thenReturn(betResult);

        BetResultDto result = gameActivityService.submitBet(betDto);

        //then
        assertEquals(startingBalance.add(winAmount), walletModel.getBalance());
        assertEquals(betResult, result);
    }
}
