package com.game.nim.usecase.in;

public interface Stake {

    /**
     * get the current amount of sticks on the stake
     * @return the current amount of sticks on the stake
     */
    long get();

    /**
     * overwrite the current amount of sticks on the stake
     * @param sticks the amount of sticks to set
     */
    void set(long sticks);

    /**
     * subtract the amount of sticks from the stake and return the remainder
     * @param amount number of sticks to remove from stake
     * @return the remaining count of sticks on the stake
     */
    long reduce(long amount);

}
