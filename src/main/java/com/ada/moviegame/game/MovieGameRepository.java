package com.ada.moviegame.game;

import com.ada.moviegame.game.domain.MovieGame;
import com.ada.moviegame.game.domain.projection.MovieGameTotalScore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGameRepository
    extends PagingAndSortingRepository<MovieGame, Integer>, JpaRepository<MovieGame, Integer> {

  Optional<MovieGame> findByUsernameAndFinished(String username, boolean isFinished);

  @Query(
      value =
          """
          select new com.ada.moviegame.game.domain.projection.MovieGameTotalScore(sum(mg.score), mg.username)
            from MovieGame mg
            group by mg.username
          """)
  List<MovieGameTotalScore> getTotalScoreByUser();
}
