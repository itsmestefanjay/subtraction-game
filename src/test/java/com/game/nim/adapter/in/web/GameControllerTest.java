package com.game.nim.adapter.in.web;

import com.game.nim.domain.model.GameStatus;
import com.game.nim.usecase.in.Move;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    private static final String API_ROOT = "/nim-game/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Move move;

    @Test
    public void test_if_restart_works() throws Exception {
        // setup
        when(move.restartGame(anyLong())).thenReturn(new GameStatus(19, "restarted"));

        // expect
        this.mockMvc.perform(put(API_ROOT + "/restart")
                        .content("{ \"initialSticks\": 19 }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"sticksLeft\" : 19 }"))
                .andExpect(content().json("{ \"message\" :  \"restarted\" }"));
    }

    @Test
    public void test_if_status_is_returned() throws Exception {
        // setup
        when(move.drawSticks(anyLong())).thenReturn(new GameStatus(15, "message"));

        // expect
        this.mockMvc.perform(
                        put(API_ROOT + "/draw")
                                .content("{ \"sticks\": 1 }")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"sticksLeft\" : 15 }"))
                .andExpect(content().json("{ \"message\" :  \"message\" }"));
    }

    @Test
    public void test_error_if_value_no_number() throws Exception {
        // expect
        this.mockMvc.perform(
                        put(API_ROOT + "/draw")
                                .content("{ \"sticks\": \"foo\" }")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{ \"errorTitle\" : \"Parse error\" }"))
                .andExpect(content().json("{ \"errors\" :  { \"Invalid arguments\" : \"Please set the property 'sticks' with a numeric value.\" } }"));
    }

    @Test
    public void test_error_if_value_too_big() throws Exception {
        // expect
        this.mockMvc.perform(
                        put(API_ROOT + "/draw")
                                .content("{ \"sticks\": 6 }")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{ \"errorTitle\" : \"Invalid arguments\" }"))
                .andExpect(content().json("{ \"errors\" :  { \"sticks\" : \"The value must be < 4\" } }"));
    }

    @Test
    public void test_error_if_value_too_small() throws Exception {
        // expect
        this.mockMvc.perform(
                        put(API_ROOT + "/draw")
                                .content("{ \"sticks\": 0 }")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{ \"errorTitle\" : \"Invalid arguments\" }"))
                .andExpect(content().json("{ \"errors\" :  { \"sticks\" : \"The value must be > 0\" } }"));
    }

}