package com.ada.moviegame.game;

import static com.ada.moviegame.game.domain.MovieGame.buildNewMovieGame;
import static com.ada.moviegame.game.domain.MovieGameTurn.buildMovieGameTurn;

import com.ada.moviegame.exception.GameEndedException;
import com.ada.moviegame.exception.NotFoundException;
import com.ada.moviegame.exception.WrongUserException;
import com.ada.moviegame.game.controller.dto.GameRankingResponse;
import com.ada.moviegame.game.controller.dto.WinnerOption;
import com.ada.moviegame.game.domain.MovieGame;
import com.ada.moviegame.game.domain.MovieGameTurn;
import com.ada.moviegame.game.domain.projection.MovieGameTotalScore;
import com.ada.moviegame.imdb.service.ImdbService;
import com.ada.moviegame.imdb.service.MovieDataService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieGameService {

  private final MovieGameRepository movieGameRepository;
  private final MovieDataService movieDataService;
  private final ImdbService imdbService;

  @Value("${game.max-allowed-errors}")
  private Integer maxAllowedErrors;

  public MovieGame newMovieGame() {
    String authenticatedUserName = getAuthenticatedUsername();
    log.info("Authenticated user {}", authenticatedUserName);

    return movieGameRepository
        .findByUsernameAndFinished(authenticatedUserName, false)
        .map(
            movieGame -> {
              log.info("User {} already have a ongoing game {}", authenticatedUserName, movieGame);
              return movieGame;
            })
        .orElseGet(() -> movieGameRepository.save(buildNewMovieGame(authenticatedUserName)));
  }

  public MovieGame getMovieGame(Integer id) {
    log.info("Search movie Game with id {}", id);
    return movieGameRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  public MovieGameTurn newMovieGameTurn(Integer movieGameId) {
    MovieGame movieGame = getMovieGame(movieGameId);
    validateAuthenticatedUser(movieGame);
    if (movieGame.canCreateNewTurn(maxAllowedErrors)) {
      return createMovieGameTurn(movieGameId, movieGame);
    }

    return movieGame.getLatestNotPlayedTurn();
  }

  private MovieGameTurn createMovieGameTurn(Integer movieGameId, MovieGame movieGame) {
    String imdbId1 = movieDataService.getRandomMovieImdbId();
    String imdbId2 = getSecondMovieImdbId(movieGame, imdbId1);
    MovieGameTurn movieGameTurn = buildMovieGameTurn(movieGameId, imdbId1, imdbId2, imdbService);
    movieGame.addGameTurn(movieGameTurn);
    movieGameRepository.save(movieGame);
    log.info("new game turn for game {} is {}", movieGameId, movieGameTurn);
    return getMovieGame(movieGameId).getLatestNotPlayedTurn();
  }

  private String getSecondMovieImdbId(MovieGame movieGame, String imdbId1) {
    log.info("getting second movie imdb id");
    String imdbId2 = movieDataService.getRandomMovieImdbId();
    List<String> movieAlreadyPlayedList = movieGame.getMovieAlreadyPlayedList(imdbId1);
    while (movieAlreadyPlayedList.contains(imdbId2)) {
      log.info("this movie already played");
      imdbId2 = movieDataService.getRandomMovieImdbId();
    }
    return imdbId2;
  }

  public MovieGameTurn playGameTurn(Integer movieGameId, Integer gameTurnId, WinnerOption option) {
    MovieGame movieGame = getMovieGame(movieGameId);
    validateAuthenticatedUser(movieGame);
    if (!movieGame.maxErrorsReached(maxAllowedErrors)) {
      return playMovieGameTurn(gameTurnId, option, movieGame);
    }
    throw new GameEndedException();
  }

  private MovieGameTurn playMovieGameTurn(
      Integer gameTurnId, WinnerOption option, MovieGame movieGame) {
    MovieGameTurn movieGameTurn = movieGame.getGameTurn(gameTurnId);
    if (!movieGameTurn.alreadyPlayed()) {
      playMovieTurn(option, movieGame, movieGameTurn);
    }
    return movieGameTurn;
  }

  private void playMovieTurn(
      WinnerOption option, MovieGame movieGame, MovieGameTurn movieGameTurn) {
    movieGameTurn.setScored(option);
    movieGameRepository.save(movieGame);
    log.info("played movie turn {}", movieGameTurn);
  }

  public MovieGame finishMovieGame(Integer movieGameId) {
    MovieGame movieGame = getMovieGame(movieGameId);
    validateAuthenticatedUser(movieGame);
    movieGame.finish();
    log.info("finished game {}", movieGame);
    return movieGameRepository.save(movieGame);
  }

  public List<GameRankingResponse> getRanking() {
    List<MovieGameTotalScore> totalScoreList = movieGameRepository.getTotalScoreByUser();
    totalScoreList.sort(Comparator.comparing(MovieGameTotalScore::totalScore).reversed());
    int position = 1;
    List<GameRankingResponse> gameRankingResponseList = new ArrayList<>();
    for (MovieGameTotalScore mgts : totalScoreList) {
      gameRankingResponseList.add(
          new GameRankingResponse(mgts.username(), mgts.totalScore(), position++));
    }
    return gameRankingResponseList;
  }

  private static String getAuthenticatedUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }

  private void validateAuthenticatedUser(MovieGame movieGame) {
    String username = getAuthenticatedUsername();
    if (!movieGame.getUsername().equals(username)) {
      throw new WrongUserException();
    }
  }
}
