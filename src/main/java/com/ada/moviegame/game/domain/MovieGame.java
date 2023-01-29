package com.ada.moviegame.game.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MovieGame {

  @Id @GeneratedValue private Integer id;
  private String username;
  private LocalDateTime startedAt;
  private Integer score;
  private boolean finished;

  @OneToMany(mappedBy = "movieGame", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MovieGameTurn> gameTurns;

  public long calculateScore() {
    return gameTurns.stream().filter(MovieGameTurn::isScored).count();
  }

  public long calculateErrors() {
    return gameTurns.stream().filter(movieGameTurn -> !movieGameTurn.isScored()).count();
  }

  public static MovieGame buildNewMovieGame(String username) {
    return MovieGame.builder().username(username).startedAt(LocalDateTime.now()).build();
  }
}
