package com.ada.moviegame.imdb;

import com.ada.moviegame.imdb.dto.ImdbResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "imdb-client", url = "${client.imdb.url}")
public interface ImdbClient {

  @GetMapping
  ImdbResponse getMovieInfo(@RequestParam("i") String movieId, @RequestParam String apikey);
}
