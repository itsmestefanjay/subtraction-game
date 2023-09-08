package com.game.nim.domain.service;

import com.game.nim.domain.model.GameStatus;
import com.game.nim.usecase.in.Move;
import com.game.nim.usecase.in.Stake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class PlayService implements Move {

    private final static Logger logger = LoggerFactory.getLogger(PlayService.class);
    private final Stake stake;
    private final boolean expertMode;

    public PlayService(Stake stake, boolean expertMode) {
        this.stake = stake;
        this.expertMode = expertMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameStatus drawSticks(long sticks) {
        if (stake.get() > 0) {
            return play(sticks);
        } else {
            return new GameStatus(0, "No more sticks left. Restart the game.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameStatus restartGame(long initialSticks) {
        stake.set(initialSticks);
        logger.info("Sticks reset to {}", initialSticks);
        return new GameStatus(initialSticks, "Your turn. Pick some sticks.");
    }

    private GameStatus play(long sticksToTake) {
        long leftOverPlayer = stake.reduce(sticksToTake);
        logUserProgress(sticksToTake);
        if (leftOverPlayer <= 0) {
            return new GameStatus(leftOverPlayer, "You took the last stick! You lose...");
        } else {
            long computerMove = expertMode ? calculateSmartMove() : calculateRandomMove();
            long leftOverComputer = stake.reduce(computerMove);
            logComputerProgress(computerMove);
            if (leftOverComputer > 0) {
                return new GameStatus(leftOverComputer, String.format("Computer took %s. Your turn again.", getStickWord(computerMove)));
            } else {
                return new GameStatus(leftOverComputer, "Computer took the last stick. You win.");
            }
        }
    }

    private long calculateRandomMove() {
        // offset by 1 so we don't take 0 sticks
        int randomTake = new Random().nextInt(3) + 1;
        // make sure that we take only as many sticks that are available
        return Math.min(stake.get(), randomTake);
    }

    private long calculateSmartMove() {
        long sticksLeft = stake.get();
        // try that a multiple of 4 sticks is left
        long sticksToTake = sticksLeft % 4;
        // take at least one stick
        sticksToTake = Math.max(1, sticksToTake);
        // never take the last stick, only if necessary
        if (sticksToTake > 1 && sticksLeft == sticksToTake) {
            sticksToTake--;
        }
        return sticksToTake;
    }

    private void logUserProgress(long sticksToTake) {
        logProgress("Player", sticksToTake);
    }

    private void logComputerProgress(long sticksToTake) {
        logProgress("Computer", sticksToTake);
    }

    private void logProgress(String player, long sticksToTake) {
        logger.info("{} took {}. {} left.", player, getStickWord(sticksToTake), getStickWord(stake.get()));
    }

    private String getStickWord(long amount) {
        return String.format("%d stick%s", amount, amount == 1 ? "" : "s");
    }
}
