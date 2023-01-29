package com.ada.moviegame.imdb.service;

import com.ada.moviegame.imdb.ImdbClient;
import com.ada.moviegame.imdb.dto.ImdbResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImdbService {

  @Value("${client.imdb.api-key}")
  private String apiKey;

  private final ImdbClient imdbClient;

  public ImdbResponse getMovieResponse(String id) {
    return imdbClient.getMovieInfo(id, apiKey);
  }
}
