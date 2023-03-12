package com.betting.javabettingapi.model;

import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
@Entity
public class GameActivityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long gameActivityId;

    private BigDecimal betAmount;

    @Enumerated(EnumType.ORDINAL)
    private Currency currency;

    @OneToOne
    private PlayerModel player;

    @OneToOne
    private GameModel game;

    private BigDecimal winAmount;

    @Enumerated(EnumType.ORDINAL)
    private BetStatus outcome;

    private BigDecimal playerBalanceAfter;

    public GameActivityModel() {

    }
}
