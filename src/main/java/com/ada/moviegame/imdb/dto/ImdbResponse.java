package com.ada.moviegame.imdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImdbResponse {

  private static final String NOT_AVAILABLE = "N/A";

  @JsonProperty(value = "Title")
  String title;

  @JsonProperty(value = "Year")
  String year;

  String imdbRating;
  String imdbVotes;

  public void setImdbVotes(String imdbVotes) {
    this.imdbVotes = imdbVotes.replaceAll(",", "");
  }

  public Double calculateMoviePoints() {
    if (ratingsInfoNotAvailable()) return (double) 0;
    return Double.parseDouble(getImdbRating()) * Double.parseDouble(getImdbVotes());
  }

  private boolean ratingsInfoNotAvailable() {
    return getImdbRating().equals(NOT_AVAILABLE) || getImdbVotes().equals(NOT_AVAILABLE);
  }
}
