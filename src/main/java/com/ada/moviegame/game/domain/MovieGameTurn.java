package com.ada.moviegame.game.domain;

import static com.ada.moviegame.game.controller.dto.WinnerOption.MOVIE_1;
import static com.ada.moviegame.game.controller.dto.WinnerOption.MOVIE_2;
import static jakarta.persistence.FetchType.EAGER;

import com.ada.moviegame.game.controller.dto.WinnerOption;
import com.ada.moviegame.imdb.dto.ImdbResponse;
import com.ada.moviegame.imdb.service.ImdbService;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "movieGame")
public class MovieGameTurn {

  @Id @GeneratedValue private Integer id;
  private String movieImdbId1;
  private String movieTitle1;
  private double movie1Score;
  private String movieImdbId2;
  private String movieTitle2;
  private double movie2Score;
  private boolean scored;
  private LocalDateTime startedAt;
  private LocalDateTime playedAt;

  @ManyToOne(fetch = EAGER)
  private MovieGame movieGame;

  @PrePersist
  public void prePersist() {
    this.startedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.playedAt = LocalDateTime.now();
  }

  public boolean alreadyPlayed() {
    return playedAt != null;
  }

  public boolean containsImdbMovieId(String movieId) {
    return getMovieImdbId1().equals(movieId) || getMovieImdbId2().equals(movieId);
  }

  public String getOpositor(String movieId) {
    if (!containsImdbMovieId(movieId)) return null;
    return getMovieImdbId1().equals(movieId) ? getMovieImdbId2() : getMovieImdbId1();
  }

  public static MovieGameTurn buildMovieGameTurn(
      Integer movieGameId, String imdbId1, String imdbId2, ImdbService imdbService) {
    ImdbResponse imdbResponse1 = imdbService.getMovieResponse(imdbId1);
    ImdbResponse imdbResponse2 = imdbService.getMovieResponse(imdbId2);

    return MovieGameTurn.builder()
        .movieGame(MovieGame.builder().id(movieGameId).build())
        .movieImdbId1(imdbId1)
        .movie1Score(imdbResponse1.calculateMoviePoints())
        .movieTitle1(imdbResponse1.getTitle())
        .movieImdbId2(imdbId2)
        .movie2Score(imdbResponse2.calculateMoviePoints())
        .movieTitle2(imdbResponse2.getTitle())
        .build();
  }

  public void setScored(WinnerOption option) {
    this.scored = option.equals(getWinner());
    this.playedAt = LocalDateTime.now();
  }

  private WinnerOption getWinner() {
    return movie1Score > movie2Score ? MOVIE_1 : MOVIE_2;
  }
}
