package com.betting.javabettingapi.controller;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.service.GameActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/gameactivity")
@RestController
public class GameActivityController {
    private final GameActivityService gameActivityService;

    public GameActivityController(@Autowired GameActivityService gameActivityService){
        this.gameActivityService = gameActivityService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BetResultDto submitBet(@RequestBody BetDto betDto){
        BetResultDto resultDto = gameActivityService.submitBet(betDto);

        return resultDto;
    }


}
