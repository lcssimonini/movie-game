package com.ada.moviegame.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ada.moviegame.exception.NotFoundException;
import com.ada.moviegame.game.domain.MovieGame;
import com.ada.moviegame.game.domain.MovieGameTurn;
import com.ada.moviegame.imdb.dto.ImdbResponse;
import com.ada.moviegame.imdb.service.ImdbService;
import com.ada.moviegame.imdb.service.MovieDataService;
import com.ada.moviegame.security.domain.GameUser;
import com.ada.moviegame.security.domain.Role;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MovieGameServiceTest {

  private final String username = "testuser";
  @InjectMocks private MovieGameService movieGameService;
  @Mock private MovieGameRepository movieGameRepository;
  @Mock private MovieDataService movieDataService;
  @Mock private ImdbService imdbService;

  private void initAuthUser() {
    GameUser user = GameUser.builder().username(username).role(Role.USER).build();
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authToken);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void shouldCreateNewMovieGame() {
    initAuthUser();
    MovieGame movieGame = MovieGame.builder().username(username).build();
    when(movieGameRepository.findByUsernameAndFinished(username, false))
        .thenReturn(Optional.empty());
    when(movieGameRepository.save(any(MovieGame.class))).thenReturn(movieGame);
    MovieGame savedMovieGame = movieGameService.newMovieGame();
    assertEquals(username, savedMovieGame.getUsername());
    assertEquals(0, savedMovieGame.getGameTurns().size());
  }

  @Test
  void shouldNotCreateNewMovieGameGameAlreadyCreated() {
    initAuthUser();
    MovieGame movieGame = MovieGame.builder().username(username).build();
    when(movieGameRepository.findByUsernameAndFinished(username, false))
        .thenReturn(Optional.of(movieGame));
    MovieGame savedMovieGame = movieGameService.newMovieGame();
    verify(movieGameRepository, times(0)).save(any(MovieGame.class));
    assertEquals(username, savedMovieGame.getUsername());
    assertEquals(0, savedMovieGame.getGameTurns().size());
  }

  @Test
  void shouldGetMovieGameById() {
    int gameId = 1;
    MovieGame movieGame = MovieGame.builder().id(gameId).username(username).build();
    when(movieGameRepository.findById(gameId)).thenReturn(Optional.of(movieGame));
    MovieGame savedMovieGame = movieGameService.getMovieGame(gameId);
    assertEquals(movieGame, savedMovieGame);
  }

  @Test
  void shouldNotGetMovieGameByIdNotFound() {
    int gameId = 1;
    when(movieGameRepository.findById(gameId)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> movieGameService.getMovieGame(gameId));
  }

  @Test
  void shouldCreateNewMovieGameTurn() {
    ReflectionTestUtils.setField(movieGameService, "maxAllowedErrors", 1);
    initAuthUser();
    int gameId = 1;
    String movieId = "movieid";
    MovieGame movieGame = MovieGame.builder().id(gameId).username(username).build();
    when(movieDataService.getRandomMovieImdbId()).thenReturn(movieId);
    when(movieGameRepository.findById(gameId)).thenReturn(Optional.of(movieGame));
    ImdbResponse imdbResponse =
        ImdbResponse.builder().title("title").imdbVotes("10").imdbRating("10").build();
    when(imdbService.getMovieResponse(movieId)).thenReturn(imdbResponse);
    MovieGameTurn movieGameTurn = movieGameService.newMovieGameTurn(1);

    assertEquals(movieId, movieGameTurn.getMovieImdbId1());
    assertEquals(100, movieGameTurn.getMovie1Score());
  }

  @Test
  void playGameTurn() {}

  @Test
  void finishMovieGame() {}

  @Test
  void getRanking() {}
}
