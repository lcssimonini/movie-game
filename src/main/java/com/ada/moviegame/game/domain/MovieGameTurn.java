package com.ada.moviegame.game.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MovieGameTurn {

  @Id @GeneratedValue private Integer id;
  private String movieId1;
  private String movieId2;
  private boolean scored;
  private LocalDateTime createdAt;
  private LocalDateTime playedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private MovieGame movieGame;
}
