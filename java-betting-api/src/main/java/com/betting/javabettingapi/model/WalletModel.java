package com.betting.javabettingapi.model;

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
public class WalletModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private PlayerModel player;

    private BigDecimal balance;

    @Enumerated(EnumType.ORDINAL)
    private Currency currency;
}
