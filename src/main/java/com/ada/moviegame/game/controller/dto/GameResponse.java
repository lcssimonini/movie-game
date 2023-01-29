package com.ada.moviegame.game.controller.dto;

import com.ada.moviegame.game.domain.MovieGame;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

  private String username;
  private LocalDateTime startedAt;
  private Integer score;
  private Integer totalTurns;

  public static GameResponse from(MovieGame movieGame) {
    return GameResponse.builder()
        .score(movieGame.getScore())
        .totalTurns(movieGame.getGameTurns().size())
        .startedAt(movieGame.getStartedAt())
        .username(movieGame.getUsername())
        .build();
  }
}
