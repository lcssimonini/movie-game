package com.ada.moviegame.game.controller;

import com.ada.moviegame.game.MovieGameService;
import com.ada.moviegame.game.domain.MovieGame;
import com.ada.moviegame.game.domain.MovieGameTurn;
import com.ada.moviegame.imdb.dto.ImdbResponse;
import com.ada.moviegame.imdb.service.ImdbService;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

  private final ImdbService imdbService;

  private final MovieGameService movieGameService;

  @PostMapping("/new-game")
  public MovieGame initiateNewGame() {
    return movieGameService.newMovieGame();
  }

  @PostMapping("/{movieGameId}/new-turn")
  public MovieGameTurn getMovieTurn(@PathVariable Integer movieGameId) {
    MovieGame game = movieGameService.getMovieGame(movieGameId);
    MovieGameTurn movieGameTurn = MovieGameTurn.builder().movieId1(getRandomMovieId()).build();

    return MovieGameTurn.builder().build();
  }

  @GetMapping("/movie")
  public ImdbResponse getMovieResponse(@RequestParam String movieId) {
    return imdbService.getMovieResponse(movieId);
  }

  //  0000001

  private String getRandomMovieId() {
    int min = 1;
    int max = 2000;
    Integer movieId = new Random().nextInt(max - min + 1) + min;
    return String.format("%07d", movieId);
  }
}
