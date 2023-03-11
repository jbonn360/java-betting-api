package com.betting.javabettingapi.model;

import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GameActivityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal betAmount;

    @Enumerated(EnumType.ORDINAL)
    private Currency currency;

    @OneToOne
    private PlayerModel player;

    @Enumerated(EnumType.ORDINAL)
    private BetStatus betStatus;

    @OneToOne
    private GameModel game;
}
