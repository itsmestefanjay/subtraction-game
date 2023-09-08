package com.game.nim.adapter.in.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameErrorResponse {

    private String errorTitle;
    private Map<String, String> errors;

}
