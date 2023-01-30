package com.ada.moviegame.game.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.ada.moviegame.game.controller.dto.WinnerOption;
import com.ada.moviegame.imdb.ImdbClient;
import com.ada.moviegame.imdb.dto.ImdbResponse;
import com.ada.moviegame.imdb.service.ImdbService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovieGameTurnTest {

  @Mock private ImdbClient imdbClient;
  @InjectMocks private ImdbService imdbService;

  @Test
  public void shouldCheckIFContainsMovie() {
    String movie1 = "movie1";
    String movie2 = "movie2";
    MovieGameTurn turn =
        MovieGameTurn.builder()
            .id(1)
            .playedAt(LocalDateTime.now())
            .movieImdbId1(movie1)
            .movieImdbId2(movie2)
            .scored(true)
            .build();

    assertTrue(turn.containsImdbMovieId(movie1));
    assertTrue(turn.containsImdbMovieId(movie2));
    assertFalse(turn.containsImdbMovieId("another"));
  }

  @Test
  public void shouldGetOpositor() {
    String movie1 = "movie1";
    String movie2 = "movie2";
    MovieGameTurn turn =
        MovieGameTurn.builder()
            .id(1)
            .playedAt(LocalDateTime.now())
            .movieImdbId1(movie1)
            .movieImdbId2(movie2)
            .scored(true)
            .build();

    assertEquals(movie2, turn.getOpositor(movie1));
    assertEquals(movie1, turn.getOpositor(movie2));
  }

  @Test
  public void shouldBuildGameTurn() {
    String movie1 = "movie1";
    String movie2 = "movie2";

    ImdbResponse imdbResponse1 =
        ImdbResponse.builder().title(movie1).imdbRating("10").imdbVotes("3").build();
    ImdbResponse imdbResponse2 =
        ImdbResponse.builder().title(movie2).imdbRating("10").imdbVotes("3").build();

    when(imdbClient.getMovieInfo(movie1, null)).thenReturn(imdbResponse1);
    when(imdbClient.getMovieInfo(movie2, null)).thenReturn(imdbResponse2);

    MovieGameTurn gameTurn = MovieGameTurn.buildMovieGameTurn(1, movie1, movie2, imdbService);

    assertEquals(movie1, gameTurn.getMovieTitle1());
    assertEquals(movie2, gameTurn.getMovieTitle2());
  }

  @Test
  public void shouldSetScored() {
    String movie1 = "movie1";
    String movie2 = "movie2";

    ImdbResponse imdbResponse1 =
        ImdbResponse.builder().title(movie1).imdbRating("10").imdbVotes("3").build();
    ImdbResponse imdbResponse2 =
        ImdbResponse.builder().title(movie2).imdbRating("1").imdbVotes("3").build();

    when(imdbClient.getMovieInfo(movie1, null)).thenReturn(imdbResponse1);
    when(imdbClient.getMovieInfo(movie2, null)).thenReturn(imdbResponse2);

    MovieGameTurn gameTurn = MovieGameTurn.buildMovieGameTurn(1, movie1, movie2, imdbService);
    gameTurn.setScored(WinnerOption.MOVIE_1);
    assertTrue(gameTurn.isScored());
  }
}
