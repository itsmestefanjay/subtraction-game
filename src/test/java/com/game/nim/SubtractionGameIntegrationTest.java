package com.game.nim;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.nim.adapter.in.web.model.GameResponse;
import okhttp3.*;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubtractionGameIntegrationTest {

    @LocalServerPort
    private int port;

    private static final String API_ROOT = "subtraction-game/api/v1";

    private final OkHttpClient client = new OkHttpClient();

    @Test
    void test_happy_path() throws IOException {
        GameResponse gameResponse = takeSticks(1);
        while (gameResponse.getSticksLeft() > 0) {
            long take = gameResponse.getSticksLeft() % 4;
            if (take == 0) {
                take = 1;
            }
            gameResponse = takeSticks(take);
        }
        Condition<String> winOrLose = new Condition<>(
                m -> m.contains("You win") || m.contains("You lose"),
                "win or lose"
        );
        assertThat(gameResponse.getMessage())
                .as("message")
                .is(winOrLose);

        // additional draw after win or loss
        GameResponse response = takeSticks(3);
        assertThat(response.getMessage())
                .as("message")
                .isEqualTo("No more sticks left. Restart the game.");
    }


    private GameResponse takeSticks(long sticks) throws IOException {
        RequestBody requestBody = RequestBody.create(
                String.format("{ \"sticks\" : %d }", sticks),
                MediaType.get("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url("http://localhost:" + port + "/" + API_ROOT + "/draw")
                .put(requestBody)
                .build();

        return executeCall(request);
    }

    private GameResponse executeCall(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            ResponseBody resBody = response.body();
            if (resBody != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(resBody.string(), GameResponse.class);
            } else {
                throw new RuntimeException();
            }
        }
    }

}
