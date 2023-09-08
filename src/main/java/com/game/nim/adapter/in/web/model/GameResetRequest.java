package com.game.nim.adapter.in.web.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResetRequest {

    @Min(value = 1, message = "The value must be > 0")
    @Max(value = Long.MAX_VALUE, message = "The value cannot be bigger than Long.MAX_VALUE")
    private long initialSticks;
}
