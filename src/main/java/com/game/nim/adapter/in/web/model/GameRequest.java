package com.game.nim.adapter.in.web.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {

    @Min(value = 1, message = "The value must be > 0")
    @Max(value = 3, message = "The value must be < 4")
    private long sticks;
}
