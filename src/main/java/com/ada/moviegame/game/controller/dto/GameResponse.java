package com.ada.moviegame.game.controller.dto;

import com.ada.moviegame.game.domain.MovieGame;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

  private Integer id;
  private String username;
  private LocalDateTime startedAt;
  private Integer score;
  private Integer totalTurns;

  public static GameResponse from(MovieGame movieGame) {
    return GameResponse.builder()
        .id(movieGame.getId())
        .score(movieGame.getScore())
        .totalTurns(getTotalTurns(movieGame))
        .startedAt(movieGame.getStartedAt())
        .username(movieGame.getUsername())
        .build();
  }

  private static int getTotalTurns(MovieGame movieGame) {
    return Optional.ofNullable(movieGame.getGameTurns()).map(List::size).orElse(0);
  }
}
