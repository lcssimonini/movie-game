package com.ada.moviegame.game;

import static org.junit.jupiter.api.Assertions.*;

import com.ada.moviegame.imdb.service.ImdbService;
import com.ada.moviegame.imdb.service.MovieDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieGameServiceTest {

  @InjectMocks private MovieGameService movieGameService;
  @Mock private MovieGameRepository movieGameRepository;
  @Mock private MovieDataService movieDataService;
  @Mock private ImdbService imdbService;

  @Test
  void shouldCreateNewMovieGame() {}

  @Test
  void getMovieGame() {}

  @Test
  void newMovieGameTurn() {}

  @Test
  void playGameTurn() {}

  @Test
  void finishMovieGame() {}

  @Test
  void getRanking() {}
}
