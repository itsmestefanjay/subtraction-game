package com.game.nim.domain;

import com.game.nim.domain.service.StakeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StakeRepositoryTest {

    @Test
    void test_initial_value_is_as_expected() {
        // given
        StakeRepository gameStateService = new StakeRepository();

        // expect
        Assertions.assertThat(gameStateService.get()).isEqualTo(13);
    }

    @Test
    void test_start_value_is_as_expected() {
        // given
        StakeRepository gameStateService = new StakeRepository(15);

        // expect
        Assertions.assertThat(gameStateService.get()).isEqualTo(15);
    }

    @ParameterizedTest(name = "setting stake to {0} results in {1} works")
    @CsvSource(value = {"14,14", "100,100", "-1,0"})
    void test_stick_state_can_be_set_as_expected(long value, long rest) {
        // given
        StakeRepository gameStateService = new StakeRepository();
        // when
        gameStateService.set(value);
        // then
        Assertions.assertThat(gameStateService.get()).as("stick count").isEqualTo(rest);
    }

    @ParameterizedTest(name = "removing {1} from amount of {0} leaves {2}")
    @CsvSource(value = {"13,1,12", "13,0,13", "13,13,0", "0,1,0"})
    void test_stick_state_can_be_modified_as_expected(long sticks, long reduce, long rest) {
        // given
        StakeRepository gameStateService = new StakeRepository(sticks);
        // when
        long actualRest = gameStateService.reduce(reduce);
        // then
        Assertions.assertThat(actualRest).as("stick count").isEqualTo(rest);
    }
}