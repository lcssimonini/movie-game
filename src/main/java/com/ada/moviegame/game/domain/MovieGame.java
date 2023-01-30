package com.ada.moviegame.game.domain;

import static jakarta.persistence.FetchType.EAGER;

import com.ada.moviegame.exception.NotFoundException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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
  private Integer score;
  private boolean finished;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;

  @PrePersist
  public void prePersist() {
    this.startedAt = LocalDateTime.now();
  }

  @OneToMany(mappedBy = "movieGame", cascade = CascadeType.ALL, orphanRemoval = true, fetch = EAGER)
  private List<MovieGameTurn> gameTurns;

  public long calculateScore() {
    return gameTurns.stream()
        .filter(movieGameTurn -> movieGameTurn.alreadyPlayed() && movieGameTurn.isScored())
        .count();
  }

  public long calculateErrors() {
    return gameTurns.stream()
        .filter(movieGameTurn -> movieGameTurn.alreadyPlayed() && !movieGameTurn.isScored())
        .count();
  }

  public boolean canCreateNewTurn(Integer maxAllowedErrors) {
    return noUnplayedTurns() && !maxErrorsReached(maxAllowedErrors);
  }

  private boolean noUnplayedTurns() {
    return getGameTurns().stream().noneMatch(Predicate.not(MovieGameTurn::alreadyPlayed));
  }

  public boolean maxErrorsReached(Integer maxAllowedErrors) {
    return calculateErrors() >= maxAllowedErrors;
  }

  public List<String> getMovieAlreadyPlayedList(String movieId) {
    return getGameTurns().stream()
        .filter(movieGameTurn -> movieGameTurn.containsImdbMovieId(movieId))
        .map(movieGameTurn -> movieGameTurn.getOpositor(movieId))
        .toList();
  }

  public static MovieGame buildNewMovieGame(String username) {
    return MovieGame.builder().username(username).build();
  }

  public MovieGameTurn getLatestNotPlayedTurn() {
    List<MovieGameTurn> gameTurns = getGameTurns();
    return gameTurns.stream()
        .filter(movieGameTurn -> !movieGameTurn.alreadyPlayed())
        .max(Comparator.comparing(MovieGameTurn::getStartedAt))
        .orElseGet(() -> gameTurns.get(gameTurns.size() - 1));
  }

  public void addGameTurn(MovieGameTurn movieGameTurn) {
    this.getGameTurns().add(movieGameTurn);
  }

  public MovieGameTurn getGameTurn(Integer gameTurnId) {
    return getGameTurns().stream()
        .filter(movieGameTurn -> movieGameTurn.getId().equals(gameTurnId))
        .findFirst()
        .orElseThrow(
            () -> new NotFoundException("game turn with id " + gameTurnId + " does not exist"));
  }

  public void finish() {
    if (!this.isFinished()) {
      this.finished = true;
      this.score = getGameScore();
      this.setFinishedAt(LocalDateTime.now());
    }
  }

  private int getGameScore() {
    return (int) getGameTurns().stream().filter(MovieGameTurn::isScored).count();
  }
}
