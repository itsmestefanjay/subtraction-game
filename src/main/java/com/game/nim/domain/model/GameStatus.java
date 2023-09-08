package com.game.nim.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameStatus {

    private long sticksLeft;
    private String statusMessage;

}
