package com.ada.moviegame;

import com.ada.moviegame.imdb.service.MovieDataService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Slf4j
@EnableWebSecurity
@EnableFeignClients
@SpringBootApplication
public class MovieGameApplication {

  @Autowired private MovieDataService movieDataService;

  public static void main(String[] args) {
    SpringApplication.run(MovieGameApplication.class, args);
  }

  @PostConstruct
  public void init() {
    log.info("loading movie data");
    movieDataService.insertMovieData();
  }
}
