package com.game.nim.usecase.in;

import com.game.nim.domain.model.GameStatus;

public interface Move {

    /**
     * Draw an amount of sticks from the stake
     * @param sticks the amount to subtract from the stake
     * @return the status of the game after the draw
     */
    GameStatus drawSticks(long sticks);

    /**
     * Reset the amount of sticks to the defined amount
     * @param initialSticks amount of sticks to set for the game start
     * @return the status of the game after the reset
     */
    GameStatus restartGame(long initialSticks);
}
