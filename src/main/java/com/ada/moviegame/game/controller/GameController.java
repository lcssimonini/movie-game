package com.ada.moviegame.game.controller;

import com.ada.moviegame.game.MovieGameService;
import com.ada.moviegame.game.controller.dto.GameRankingResponse;
import com.ada.moviegame.game.controller.dto.GameResponse;
import com.ada.moviegame.game.controller.dto.GameTurnResponse;
import com.ada.moviegame.game.controller.dto.PlayTurnRequest;
import com.ada.moviegame.game.domain.MovieGameTurn;
import com.ada.moviegame.imdb.dto.ImdbResponse;
import com.ada.moviegame.imdb.service.ImdbService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

  private final ImdbService imdbService;

  private final MovieGameService movieGameService;

  @PostMapping("/new-game")
  public GameResponse initiateNewGame() {
    return GameResponse.from(movieGameService.newMovieGame());
  }

  @PostMapping("/{movieGameId}/finish-game")
  public GameResponse endGame(@PathVariable Integer movieGameId) {
    return GameResponse.from(movieGameService.finishMovieGame(movieGameId));
  }

  @PostMapping("/{movieGameId}/new-turn")
  public GameTurnResponse getMovieTurn(@PathVariable Integer movieGameId) {
    MovieGameTurn movieGameTurn = movieGameService.newMovieGameTurn(movieGameId);
    log.info("Game turn for game id {}: {}", movieGameId, movieGameTurn);
    return GameTurnResponse.from(movieGameTurn);
  }

  @PostMapping("/{movieGameId}/play-turn/{gameTurnId}")
  public GameTurnResponse playMovieTurn(
      @PathVariable Integer movieGameId,
      @PathVariable Integer gameTurnId,
      @RequestBody PlayTurnRequest playTurnRequest) {
    MovieGameTurn movieGameTurn =
        movieGameService.playGameTurn(movieGameId, gameTurnId, playTurnRequest.getPlayOption());
    log.info("Game turn for game id {}: {}", movieGameId, movieGameTurn);
    return GameTurnResponse.from(movieGameTurn);
  }

  @GetMapping
  public List<GameRankingResponse> getGameRanking() {
    return movieGameService.getRanking();
  }

  @GetMapping("/movie")
  public ImdbResponse getMovieResponse(@RequestParam String movieId) {
    return imdbService.getMovieResponse(movieId);
  }
}
