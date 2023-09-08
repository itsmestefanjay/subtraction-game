package com.game.nim.domain.service;

import com.game.nim.usecase.in.Stake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class StakeRepository implements Stake {

    private static final Logger logger = LoggerFactory.getLogger(StakeRepository.class);

    public static final int DEFAULT_AMOUNT = 13;
    private final AtomicLong sticksLeft;

    public StakeRepository() {
        sticksLeft = new AtomicLong(DEFAULT_AMOUNT);
        logger.info("Stake initialized with default amount {}", DEFAULT_AMOUNT);
    }

    public StakeRepository(long sticks) {
        sticksLeft = new AtomicLong(sticks);
        logger.info("Stake initialized with {}", sticks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(long sticks) {
        sticksLeft.set(Math.max(0, sticks));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long get() {
        return sticksLeft.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long reduce(long amount) {
        long currentAmount = sticksLeft.get();
        sticksLeft.set(Math.max(0, currentAmount - amount));
        return sticksLeft.get();
    }
}
