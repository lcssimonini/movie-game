package com.ada.moviegame.game;

import com.ada.moviegame.game.domain.GameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

  @GetMapping
  public GameResponse getGameResponse() {
    return GameResponse.builder().status("hello").build();
  }
}
