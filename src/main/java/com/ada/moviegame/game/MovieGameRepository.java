package com.ada.moviegame.game;

import com.ada.moviegame.game.domain.MovieGame;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGameRepository
    extends PagingAndSortingRepository<MovieGame, Integer>, JpaRepository<MovieGame, Integer> {

  Optional<MovieGame> findByUsernameAndFinished(String username, boolean isFinished);
}
