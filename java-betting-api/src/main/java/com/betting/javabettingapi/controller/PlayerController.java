package com.betting.javabettingapi.controller;

import com.betting.javabettingapi.dto.PlayerDto;
import com.betting.javabettingapi.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(@Autowired PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDto getPlayerById(@PathVariable Long playerId) {
        PlayerDto player = playerService.getPlayerById(playerId);

        return player;
    }

    @PostMapping
    public ResponseEntity<HttpHeaders> createPlayerAccount(@RequestBody PlayerDto playerDto) {
        final Long playerId = playerService.createPlayerAccount(playerDto);

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Location", String.format("/api/v1/player/%d"));

        // return location header
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
