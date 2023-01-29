package com.ada.moviegame.game;

import static com.ada.moviegame.game.domain.MovieGame.buildNewMovieGame;

import com.ada.moviegame.exception.NotFoundException;
import com.ada.moviegame.game.domain.MovieGame;
import com.ada.moviegame.game.domain.MovieGameTurn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieGameService {

  private final MovieGameRepository movieGameRepository;

  public MovieGame newMovieGame() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String authenticatedUserName = authentication.getName();
    log.info("Authenticated user {}", authenticatedUserName);

    return movieGameRepository.save(buildNewMovieGame(authenticatedUserName));
  }

  public MovieGame getMovieGame(Integer id) {
    log.info("Search movie Game with id {}", id);
    return movieGameRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  public MovieGameTurn newMovieGameTurn(Integer movieGameId) {

    MovieGame movieGame = getMovieGame(movieGameId);
    MovieGameTurn movieGameTurn = MovieGameTurn.builder().build();
    return movieGameTurn;
  }
}
