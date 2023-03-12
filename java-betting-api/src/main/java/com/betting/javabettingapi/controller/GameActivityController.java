package com.betting.javabettingapi.controller;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.service.GameActivityService;
import com.betting.javabettingapi.dto.BetResultListDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RequestMapping("/api/v1/gameactivity")
@RestController
public class GameActivityController {
    private final GameActivityService gameActivityService;

    public GameActivityController(@Autowired GameActivityService gameActivityService){
        this.gameActivityService = gameActivityService;
    }

    @GetMapping(params = { "limit", "offset" }, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public BetResultListDto getAllBets(@RequestParam("limit") int limit, @RequestParam int offset){
        BetResultListDto result = gameActivityService.getAllBets(limit, offset);

        return result;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BetResultDto submitBet(@Valid @RequestBody BetDto betDto){
        BetResultDto resultDto = gameActivityService.submitBet(betDto);

        return resultDto;
    }


}
