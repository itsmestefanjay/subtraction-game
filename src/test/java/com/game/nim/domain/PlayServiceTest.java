package com.game.nim.domain;

import com.game.nim.domain.model.GameStatus;
import com.game.nim.domain.service.StakeRepository;
import com.game.nim.domain.service.PlayService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PlayServiceTest {

    @ParameterizedTest(name = "player takes last stick with expertMode={0} works")
    @ValueSource(booleans = {true, false})
    void test_player_takes_last_stick(boolean expertMode) {
        // given
        StakeRepository gameStateService = new StakeRepository();
        gameStateService.set(1);
        PlayService service = new PlayService(gameStateService, expertMode);

        // when
        GameStatus gameStatus = service.drawSticks(1);

        // then
        assertThat(gameStatus.getSticksLeft()).as("sticks left")
                .isEqualTo(0);
        assertThat(gameStatus.getStatusMessage()).as("message")
                .isEqualTo("You took the last stick! You lose...");
    }

    @ParameterizedTest(name = "computer takes last stick with expertMode={0} works")
    @ValueSource(booleans = {true, false})
    void test_computer_takes_last_stick(boolean expertMode) {
        // given
        StakeRepository gameStateService = new StakeRepository();
        gameStateService.set(2);
        PlayService service = new PlayService(gameStateService, expertMode);

        // when
        GameStatus gameStatus = service.drawSticks(1);

        // then
        assertThat(gameStatus.getSticksLeft()).as("sticks left")
                .isEqualTo(0);
        assertThat(gameStatus.getStatusMessage()).as("message")
                .isEqualTo("Computer took the last stick. You win.");
    }

    @ParameterizedTest(name = "regular move with expertMode={0} works")
    @ValueSource(booleans = {true, false})
    void test_regular_move(boolean expertMode) {
        // given
        StakeRepository gameStateService = new StakeRepository();
        gameStateService.set(13);
        PlayService service = new PlayService(gameStateService, expertMode);

        // when
        GameStatus gameStatus = service.drawSticks(1);

        // then
        assertThat(gameStatus.getSticksLeft()).as("sticks left")
                .isGreaterThan(0);
        assertThat(gameStatus.getStatusMessage()).as("message")
                .endsWith("Your turn again.");
    }
}