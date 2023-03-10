package com.ada.moviegame.imdb.repository;

import com.ada.moviegame.imdb.domain.MovieData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDataRepository
    extends PagingAndSortingRepository<MovieData, Integer>, JpaRepository<MovieData, Integer> {

  List<MovieData> findAllByImdbIdNotIn(List<String> excludeIds);
}
