package com.game.nim.adapter.in.web;

import com.game.nim.adapter.in.web.model.GameErrorResponse;
import com.game.nim.adapter.in.web.model.GameRequest;
import com.game.nim.adapter.in.web.model.GameResetRequest;
import com.game.nim.adapter.in.web.model.GameResponse;
import com.game.nim.domain.model.GameStatus;
import com.game.nim.usecase.in.Move;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "subtraction-game/api/v1")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final Move move;

    public GameController(Move move) {
        this.move = move;
    }

    @PutMapping(path = "/draw")
    public GameResponse take(@RequestBody @Valid GameRequest gameRequest) {
        logger.debug("Request received: {}", gameRequest);
        GameStatus gameStatus = move.drawSticks(gameRequest.getSticks());
        return new GameResponse(gameStatus.getSticksLeft(), gameStatus.getStatusMessage());
    }

    @PutMapping(path = "/restart")
    public GameResponse restart(@RequestBody @Valid GameResetRequest resetRequest) {
        GameStatus gameStatus = move.restartGame(resetRequest.getInitialSticks());
        return new GameResponse(gameStatus.getSticksLeft(), gameStatus.getStatusMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<GameErrorResponse> handleMethodArgumentInvalid(MethodArgumentNotValidException e) {
        Map<String, String> errors = extractErrorsFromException(e);
        return new ResponseEntity<>(new GameErrorResponse("Invalid arguments", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<GameErrorResponse> handleParseError(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(
                new GameErrorResponse(
                        "Parse error",
                        Map.of("Invalid arguments", "Please set the property 'sticks' with a numeric value.")
                ), HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> extractErrorsFromException(MethodArgumentNotValidException e) {
        return e.getFieldErrors().stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(),
                                        String.format("Invalid value: %s", fieldError.getRejectedValue()))
                        )
                );
    }

}
