package com.betting.javabettingapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "game")
public class GameModel {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
