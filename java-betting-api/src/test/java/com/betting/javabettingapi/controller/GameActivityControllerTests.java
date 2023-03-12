package com.betting.javabettingapi.controller;

import com.betting.javabettingapi.dto.BetDto;
import com.betting.javabettingapi.dto.BetResultDto;
import com.betting.javabettingapi.dto.BetResultListDto;
import com.betting.javabettingapi.service.GameActivityServiceImpl;
import com.betting.javabettingapi.utils.BetStatus;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameActivityController.class)
public class GameActivityControllerTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private GameActivityServiceImpl gameActivityService;

    private BetResultDto betResult1 = BetResultDto.builder()
            .gameActivityId("123456")
            .gameId("1")
            .betAmount(new BigDecimal(30))
            .winAmount(new BigDecimal(40))
            .currency(Currency.EUR)
            .outcome(BetStatus.WIN)
            .playerBalanceAfter(new BigDecimal(500)).build();

    private BetResultDto betResult2 = BetResultDto.builder()
            .gameActivityId("12344456")
            .gameId("1")
            .betAmount(new BigDecimal(50))
            .winAmount(new BigDecimal(-50))
            .currency(Currency.EUR)
            .outcome(BetStatus.LOSS)
            .playerBalanceAfter(new BigDecimal(450)).build();

    private BetDto bet1 = BetDto.builder().gameActivityId(435434l)
            .betAmount(new BigDecimal(10.50))
            .currency(Currency.EUR)
            .playerId(1l)
            .gameId(1)
            .build();

    public GameActivityControllerTests(@Autowired final MockMvc mockMvc, @Autowired final ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void givenLimitAndOffsetQueryParameters_WhenGetGameActivityAPIIsCalled_ThenReturnBetResultDtoList()
            throws Exception
    {
        //given
        BetResultListDto resultList = new BetResultListDto(List.of(betResult1, betResult2));

        byte limit = 2;byte offset = 0;

        //when
        when(gameActivityService.getAllBets(limit, offset)).thenReturn(resultList);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/gameactivity")
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(resultList)));
    }

    @Test
    public void givenValidBetDto_WhenPostBetIsCalled_ThenBetIsSubmittedAndResultIsReturned() throws Exception
    {
        //when
        when(gameActivityService.submitBet(any())).thenReturn(betResult1);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/gameactivity")
                        .content(objectMapper.writeValueAsString(bet1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(betResult1)));
    }

    @Test
    public void givenNoBetDto_WhenPostBetIsCalled_ThenErrorCodeIsReturned() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/gameactivity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
