package com.betting.javabettingapi.service;

import com.betting.javabettingapi.exception.EntityNotFoundException;
import com.betting.javabettingapi.model.GameModel;
import com.betting.javabettingapi.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService{
    private final GameRepository gameRepository;
    public GameServiceImpl(
            @Autowired GameRepository gameRepository
    ){
        this.gameRepository = gameRepository;
    }

    @Override
    public GameModel getGameModelById(Integer gameId) {
        GameModel gameModel = gameRepository.findById(gameId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Game with id %d not found", gameId))
        );

        return gameModel;
    }
}
