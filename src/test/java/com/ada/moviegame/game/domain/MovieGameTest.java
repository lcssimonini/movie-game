package com.ada.moviegame.game.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class MovieGameTest {

  @Test
  public void shouldCalculateScore() {
    MovieGame game = MovieGame.builder().build();
    MovieGameTurn turn1 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(true).build();
    MovieGameTurn turn2 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(true).build();
    MovieGameTurn turn3 = MovieGameTurn.builder().scored(true).build();
    game.setGameTurns(List.of(turn1, turn2, turn3));

    assertEquals(2, game.calculateScore());
  }

  @Test
  public void shouldCalculateErrors() {
    MovieGame game = MovieGame.builder().build();
    MovieGameTurn turn1 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(true).build();
    MovieGameTurn turn2 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(false).build();
    MovieGameTurn turn3 = MovieGameTurn.builder().scored(false).build();
    game.setGameTurns(List.of(turn1, turn2, turn3));

    assertEquals(1, game.calculateErrors());
  }

  @Test
  public void shouldBeAbleToCreateTurn() {
    MovieGame game = MovieGame.builder().build();
    MovieGameTurn turn1 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(true).build();
    MovieGameTurn turn2 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(false).build();
    game.setGameTurns(List.of(turn1, turn2));

    assertTrue(game.canCreateNewTurn(2));
  }

  @Test
  public void shouldNotBeAbleToCreateTurn() {
    MovieGame game = MovieGame.builder().build();
    MovieGameTurn turn1 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(true).build();
    MovieGameTurn turn2 = MovieGameTurn.builder().scored(false).build();
    game.setGameTurns(List.of(turn1, turn2));

    assertFalse(game.canCreateNewTurn(2));
  }

  @Test
  public void doesNotHaveUnplayedTurns() {
    MovieGame game = MovieGame.builder().build();
    MovieGameTurn turn1 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(false).build();
    MovieGameTurn turn2 =
        MovieGameTurn.builder().playedAt(LocalDateTime.now()).scored(false).build();
    game.setGameTurns(List.of(turn1, turn2));

    assertTrue(game.maxErrorsReached(2));
  }

  @Test
  public void shouldGetOpositorList() {
    MovieGame game = MovieGame.builder().build();
    String movie1 = "movie1";
    String movie2 = "movie2";
    String movie3 = "movie1";
    String movie4 = "movie3";
    MovieGameTurn turn1 =
        MovieGameTurn.builder()
            .playedAt(LocalDateTime.now())
            .scored(false)
            .movieImdbId1(movie1)
            .movieImdbId2(movie2)
            .build();
    MovieGameTurn turn2 =
        MovieGameTurn.builder()
            .playedAt(LocalDateTime.now())
            .scored(false)
            .movieImdbId1(movie3)
            .movieImdbId2(movie4)
            .build();

    game.setGameTurns(List.of(turn1, turn2));

    assertEquals(2, game.getMovieAlreadyPlayedList(movie1).size());
    assertTrue(game.getMovieAlreadyPlayedList(movie1).contains(movie2));
    assertTrue(game.getMovieAlreadyPlayedList(movie1).contains(movie4));
    assertFalse(game.getMovieAlreadyPlayedList(movie1).contains(movie1));
  }

  @Test
  public void shouldGetLatestUnplayedTurn() {
    MovieGame game = MovieGame.builder().build();
    String movie1 = "movie1";
    String movie2 = "movie2";
    MovieGameTurn turn1 =
        MovieGameTurn.builder()
            .playedAt(LocalDateTime.now())
            .movieImdbId1(movie1)
            .scored(false)
            .build();
    MovieGameTurn turn2 = MovieGameTurn.builder().movieImdbId1(movie2).scored(false).build();
    game.setGameTurns(List.of(turn1, turn2));

    assertEquals(movie2, game.getLatestNotPlayedTurn().getMovieImdbId1());
  }

  @Test
  public void shouldGetGameTurnById() {
    MovieGame game = MovieGame.builder().build();
    String movie1 = "movie1";
    String movie2 = "movie2";
    MovieGameTurn turn1 =
        MovieGameTurn.builder()
            .id(1)
            .playedAt(LocalDateTime.now())
            .movieImdbId1(movie1)
            .scored(false)
            .build();
    MovieGameTurn turn2 = MovieGameTurn.builder().id(2).movieImdbId1(movie2).scored(false).build();
    game.setGameTurns(List.of(turn1, turn2));

    assertEquals(1, game.getGameTurn(1).getId());
  }

  @Test
  public void shouldFinishGame() {
    MovieGame game = MovieGame.builder().build();
    String movie1 = "movie1";
    String movie2 = "movie2";
    MovieGameTurn turn1 =
        MovieGameTurn.builder()
            .id(1)
            .playedAt(LocalDateTime.now())
            .movieImdbId1(movie1)
            .scored(true)
            .build();
    MovieGameTurn turn2 =
        MovieGameTurn.builder()
            .id(2)
            .playedAt(LocalDateTime.now())
            .movieImdbId1(movie2)
            .scored(false)
            .build();
    game.setGameTurns(List.of(turn1, turn2));
    game.finish();
    assertTrue(game.isFinished());
    assertNotNull(game.getFinishedAt());
    assertEquals(1, game.getScore());
  }
}
