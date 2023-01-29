package com.ada.moviegame.game.controller.dto;

import com.ada.moviegame.game.domain.MovieGameTurn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameTurnResponse {

  Integer gameTurnId;
  String movieTitle1;
  String movieTitle2;

  boolean scored;

  public static GameTurnResponse from(MovieGameTurn gameTurn) {
    return GameTurnResponse.builder()
        .gameTurnId(gameTurn.getId())
        .movieTitle1(gameTurn.getMovieTitle1())
        .movieTitle2(gameTurn.getMovieTitle2())
        .scored(gameTurn.isScored())
        .build();
  }
}
