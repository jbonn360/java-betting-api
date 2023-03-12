package com.betting.javabettingapi.controller;

import com.betting.javabettingapi.dto.PlayerDto;
import com.betting.javabettingapi.dto.WalletDto;
import com.betting.javabettingapi.service.PlayerServiceImpl;
import com.betting.javabettingapi.utils.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private PlayerServiceImpl playerService;

    public PlayerControllerTests(@Autowired final MockMvc mockMvc, @Autowired final ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void givenPlayerId_WhenGetPlayerIsCalled_ThenReturnPlayerDto()
            throws Exception
    {
        //given
        WalletDto wallet = WalletDto.builder().currency(Currency.EUR).balance(new BigDecimal(0)).build();
        PlayerDto player = PlayerDto.builder().id(123434l).wallet(wallet).username("username").build();

        //when
        when(playerService.getPlayerById(player.getId())).thenReturn(player);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/player/%d", player.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(player)));
    }

    @Test
    public void givenValidPlayerDto_WhenPostPlayerIsCalled_ThenPlayerAccountIsCreated() throws Exception
    {
        //given
        PlayerDto playerToSend = PlayerDto.builder().username("username").build();

        final long playerId = 1l;

        //when
        when(playerService.createPlayerAccount(any())).thenReturn(playerId);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/player")
                        .content(objectMapper.writeValueAsString(playerToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/api/v1/player/%d", playerId)));
    }

    @Test
    public void givenInvalidPlayerDto_WhenPostPlayerIsCalled_ThenPlayerAccountIsCreated() throws Exception
    {
        //given
        PlayerDto playerToSend = PlayerDto.builder().username("a").build();

        final long playerId = 1l;

        //when
        when(playerService.createPlayerAccount(any())).thenReturn(playerId);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/player")
                        .content(objectMapper.writeValueAsString(playerToSend))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
