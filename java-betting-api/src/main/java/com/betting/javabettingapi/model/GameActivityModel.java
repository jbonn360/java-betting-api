package com.betting.javabettingapi.model;

import com.betting.javabettingapi.utils.BetStatus;
import com.betting.javabettingapi.utils.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@Entity
public class GameActivityModel {
    public GameActivityModel(){    }
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

    private BigDecimal amountWon;

    @Enumerated(EnumType.ORDINAL)
    private BetStatus betStatus;

    private BigDecimal playerBalanceAfter;
}
