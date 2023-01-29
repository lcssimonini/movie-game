package com.ada.moviegame.imdb.service;

import com.ada.moviegame.imdb.domain.MovieData;
import com.ada.moviegame.imdb.repository.MovieDataRepository;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadMovieDataService {

  private final MovieDataRepository movieDataRepository;

  private static final String[] MOVIES_HEADERS = {
    "tconst",
    "titleType",
    "primaryTitle",
    "originalTitle",
    "isAdult",
    "startYear",
    "endYear",
    "runtimeMinutes",
    "genres"
  };

  private static final String MOVIES_CSV = "filtered_movie_data.csv";

  public void insertMovieData() {
    List<MovieData> movieDataList = loadMovies();
    long savedMovieCount = movieDataRepository.count();
    if (savedMovieCount == movieDataList.size()) {
      log.info("movies already on database");
      return;
    }

    movieDataRepository.saveAll(movieDataList);
    log.info("movies saved on database");
  }

  private List<MovieData> loadMovies() {
    Iterable<CSVRecord> records = null;
    try {
      records = getCsvRecords();
    } catch (IOException e) {
      log.error("Error reading file");
    }

    return Optional.ofNullable(records).map(mapMovieData()).orElse(new ArrayList<>());
  }

  private static Function<Iterable<CSVRecord>, List<MovieData>> mapMovieData() {
    return rs ->
        StreamSupport.stream(rs.spliterator(), false)
            .map(LoadMovieDataService::buildMovieData)
            .collect(Collectors.toList());
  }

  private static Iterable<CSVRecord> getCsvRecords() throws IOException {
    Reader in = new InputStreamReader(new ClassPathResource(MOVIES_CSV).getInputStream());
    return CSVFormat.DEFAULT.withHeader(MOVIES_HEADERS).withFirstRecordAsHeader().parse(in);
  }

  private static MovieData buildMovieData(CSVRecord record) {
    return MovieData.builder()
        .imdbId(record.get(MOVIES_HEADERS[0]))
        .title(record.get(MOVIES_HEADERS[2]))
        .year(record.get(MOVIES_HEADERS[5]))
        .build();
  }
}
